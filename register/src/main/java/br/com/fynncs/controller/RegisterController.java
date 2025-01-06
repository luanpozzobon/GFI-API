package br.com.fynncs.controller;

import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.model.User;
import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/register")
public class RegisterController {

    @PostMapping
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        try (CRUDManager manager = new CRUDManager(
                ResourceService.findResourceById(ConnectionProvider.POSTGRES, "", ResourceType.DATABASECONNECTION),
                ConnectionProvider.POSTGRES
        )) {
            RegisterService service = new RegisterService(manager);
            service.register(user);
        } catch (Exception ex) {
            throw new Exception("Error trying to register user", ex);
        }
    }
}
