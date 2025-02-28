package br.com.fynncs.controller;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.model.System;
import br.com.fynncs.model.User;
import br.com.fynncs.records.Login;
import br.com.fynncs.security.model.Authentication;
import br.com.fynncs.service.LoginService;
import br.com.fynncs.service.SystemService;
import br.com.fynncs.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private LoginService service;

    @PostMapping("/")
    public ResponseEntity<Authentication> login(
            HttpServletRequest request, @RequestBody Login loginInfo
    ) throws Exception {
        System system = LoginController.getCurrentSystem(request);

        try (CRUDManager manager = new CRUDManager(
                ResourceService.findStandardDataBaseConnectionResource(),
                ConnectionProvider.valueOf(system.getConnectionProvider())
        )) {
            this.service.initializer(manager, system);
            Authentication auth = this.service.login(loginInfo);

            if (auth == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            return ResponseEntity.ok(auth);
        } catch (Exception ex) {
            throw new Exception("Error authenticating user!", ex);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Authentication> loginWithGoogle(HttpServletRequest request, @RequestBody User user) throws Exception {
        System system = LoginController.getCurrentSystem(request);

        try (CRUDManager manager = new CRUDManager(
                ResourceService.findResourceById(ConnectionProvider.valueOf(system.getConnectionProvider()),
                        system.getDatabase(), ResourceType.DATABASECONNECTION),
                ConnectionProvider.valueOf(system.getConnectionProvider())
        )) {
            LoginService service = new LoginService(manager, system);
            Authentication auth = service.loginWithGoogle(user);

            if (auth == null)
                return ResponseEntity.ok().build();

            return ResponseEntity.ok(auth);
        } catch (Exception ex) {
            throw new Exception("Error authenticating user!", ex);
        }
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
