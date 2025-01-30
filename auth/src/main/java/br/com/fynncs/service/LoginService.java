package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.User;
import br.com.fynncs.records.Login;
import br.com.fynncs.security.model.Authentication;
import br.com.fynncs.security.token.TokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private PasswordEncoder encoder;

    private CRUDManager manager;
    private UserService userService;

    private LoginService() {
    }

    public LoginService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userService = new UserService(this.manager);
    }

    private boolean passwordMatches(String raw, String enc) {
        return this.encoder.matches(raw, enc);
    }

    private Authentication temp(User user) throws Exception {
        Authentication auth = new Authentication();
        auth.setIdentifier(user.getId().toString());


        TokenSecurity tokenSecurity = new TokenSecurity();

    }

    public Authentication login(Login loginInfo) throws Exception {
        User user = this.userService.findByLogin(loginInfo.login(), Boolean.TRUE);
        if (user == null)
            return null;



        if(!this.passwordMatches(loginInfo.password(), user.getPassword())) {
            return null;
        }

    }




}
