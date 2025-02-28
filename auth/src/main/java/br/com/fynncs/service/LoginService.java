package br.com.fynncs.service;

import br.com.fynncs.config.EncoderConfig;
import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.System;
import br.com.fynncs.model.User;
import br.com.fynncs.records.Login;
import br.com.fynncs.security.model.Authentication;
import br.com.fynncs.security.token.TokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenSecurity tokenSecurity;

    private CRUDManager manager;
    private System system;

    private UserService userService;
    private RegisterService registerService;
    private SystemService systemService;

    private LoginService() { }

    public LoginService(CRUDManager manager, System system) throws Exception {
        this.manager = manager;
        if (system == null)
            throw new Exception("Current System not Informed!");
        this.system = system;

        this.initializer();
    }

    private void initializer() throws Exception {
        this.userService = new UserService(this.manager);
        this.registerService = new RegisterService(this.manager, this.system);
        this.systemService = new SystemService();
    }

    public void initializer(CRUDManager manager, System system) throws Exception {
        this.manager = manager;
        if (system == null)
            throw new Exception("Current System not Informed!");
        this.system = system;

        this.initializer();
    }

    private boolean passwordMatches(String raw, String enc) {
        return this.encoder.matches(raw, enc);
    }

    private Authentication getAuthentication(User user, System system) throws Exception {
        Authentication auth = new Authentication();
        auth.setId(user.getId().toString());

        String token = this.tokenSecurity.createToken(auth);
        auth.setToken(token);

        return auth;
    }

    public Authentication login(Login loginInfo) throws Exception {
        User user = this.userService.findByLogin(loginInfo.login(), Boolean.TRUE);
        if (user == null)
            return null;

        if (!this.systemService.userHasAccessToSystem(user, this.system))
            return null;

        if (!this.passwordMatches(loginInfo.password(), user.getPassword()))
            return null;

        return this.getAuthentication(user, this.system);
    }

    public Authentication loginWithGoogle(User userLogin) throws Exception {
        User user = this.userService.findByLogin(userLogin.getLogin().getFirst(), userLogin.getOauthProvider(), Boolean.TRUE);
        if (user == null || !this.systemService.userHasAccessToSystem(user, this.system)) {
            this.registerService.register(userLogin);
            return null;
        }

        return this.getAuthentication(user, this.system);
    }
}
