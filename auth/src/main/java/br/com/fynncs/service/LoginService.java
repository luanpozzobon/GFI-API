package br.com.fynncs.service;

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
    private UserService userService;
    private RegisterService registerService;

    private LoginService() {
    }

    public LoginService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userService = new UserService(this.manager);
        this.registerService = new RegisterService(this.manager);
    }

    private boolean passwordMatches(String raw, String enc) {
//        return this.encoder.matches(raw, enc);
        return true;
    }


    private Authentication getAuthentication(User user, System system) throws Exception {
        Authentication auth = new Authentication();
        auth.setIdentifier(user.getId().toString());

        String token = this.tokenSecurity.createToken(auth);
        auth.setToken(token);

        return auth;
    }

    public Authentication login(Login loginInfo) throws Exception {
        User user = this.userService.findByLogin(loginInfo.login(), Boolean.TRUE);
        if (user == null)
            return null;

//        Optional<System> sys = user.getSystems().parallelStream().filter(system -> system.getName().equalsIgnoreCase(currentSystem)).findFirst();
        Optional<System> sys = Optional.empty();
        if (sys.isEmpty())
            return null;

        if (!this.passwordMatches(loginInfo.password(), user.getPassword()))
            return null;

        return this.getAuthentication(user, sys.get());
    }

    public Authentication loginWithGoogle(User userLogin) throws Exception {
        User user = this.userService.findByLogin(userLogin.getLogin().getFirst(), userLogin.getOauthProvider(), Boolean.TRUE);
        if (user == null) {
            this.registerService.register(userLogin);
            user = this.userService.findByLogin(userLogin.getLogin().getFirst(), userLogin.getOauthProvider(), Boolean.TRUE);
        }

//        Optional<System> sys = user.getSystems().parallelStream().filter(system -> system.getName().equalsIgnoreCase(currentSystem)).findFirst();
        Optional<System> sys = Optional.empty();
        if (sys.isEmpty())
            return null;

        return this.getAuthentication(user, sys.get());
    }
}
