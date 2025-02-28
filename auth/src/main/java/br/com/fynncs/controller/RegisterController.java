package br.com.fynncs.controller;

import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.model.System;
import br.com.fynncs.model.User;
import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.service.PersonService;
import br.com.fynncs.service.RegisterService;
import br.com.fynncs.service.SystemService;
import br.com.fynncs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login/register")
public class RegisterController {
    @Autowired
    private RegisterService service;

    @PostMapping("/")
    public ResponseEntity register(HttpServletRequest request, @RequestBody User user) throws Exception {
        System system = RegisterController.getCurrentSystem(request);

        try (CRUDManager manager = new CRUDManager(
                ResourceService.findStandardDataBaseConnectionResource(),
                ConnectionProvider.POSTGRES
        )) {
            this.service.initializer(manager, system);
            this.service.register(user);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            throw new Exception("Error trying to register user", ex);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirm(HttpServletRequest request, @RequestParam("id") String id) throws Exception {
        System system = RegisterController.getCurrentSystem(request);

        this.service.initializer(system);
        String message = this.service.confirm(id);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/save")
    public ResponseEntity save(User user) throws Exception {
        CRUDManager manager = null;
        try {
            manager = new CRUDManager(
                    ResourceService.findStandardDataBaseConnectionResource(),
                    ConnectionProvider.valueOf(user.getSystems().getFirst().getConnectionProvider())
            );
            UserService service = new UserService(manager);

            manager.beginRequest();
            service.persist(user);
            manager.commit();
        } catch (Exception ex) {
            if (manager != null)
                manager.revertTransaction();

            throw new Exception("Error saving user!", ex);
        } finally {
            if (manager != null)
                manager.close();
        }

        try {
            manager = new CRUDManager(
                    ResourceService.findResourceById(
                            ConnectionProvider.valueOf(user.getSystems().getFirst().getConnectionProvider()),
                            user.getSystems().getFirst().getDatabase(), ResourceType.DATABASECONNECTION),
                    ConnectionProvider.valueOf(user.getSystems().getFirst().getConnectionProvider())
            );
            PersonService service = new PersonService(manager);

            manager.beginRequest();
            service.persist(user.getPerson());
            manager.commit();
        } catch (Exception ex) {
            if (manager != null && !manager.isClosed())
                manager.revertTransaction();

            throw new Exception("Error saving person!", ex);
        } finally {
            manager.close();
        }

        return ResponseEntity.ok().build();
    }

    private static System getCurrentSystem(HttpServletRequest request) throws Exception {
        try (CRUDManager manager = new CRUDManager(
                ResourceService.findStandardDataBaseConnectionResource(),
                ConnectionProvider.POSTGRES
        )) {
            SystemService systemService = new SystemService(manager);

            return systemService.getCurrentSystem(request);
        }
    }
}
