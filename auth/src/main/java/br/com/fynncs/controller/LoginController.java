package br.com.fynncs.controller;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.enums.ResourceType;
import br.com.fynncs.core.model.Resource;
import br.com.fynncs.core.service.ResourceService;
import br.com.fynncs.model.User;
import br.com.fynncs.security.model.Authentication;
import br.com.fynncs.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/login")
public class LoginController {

//    @PostMapping
//    public ResponseEntity<Authentication> login() throws Exception {
//
//    }

    @PostMapping("/google")
    public ResponseEntity<Authentication> loginWithGoogle(@RequestBody User user) throws Exception {
        try (CRUDManager manager = new CRUDManager(
                ResourceService.findResourceById(ConnectionProvider.POSTGRES, "FYNNCS", ResourceType.DATABASECONNECTION),
                ConnectionProvider.POSTGRES
        )) {
            LoginService service = new LoginService(manager);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            throw new Exception("Error authenticating user!", ex);
        }
    }
}
