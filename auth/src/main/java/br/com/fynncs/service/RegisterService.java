package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.core.enums.ConnectionProvider;
import br.com.fynncs.core.proxy.ProxyModel;
import br.com.fynncs.model.Person;
import br.com.fynncs.model.System;
import br.com.fynncs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegisterService {
    @Autowired
    private PasswordEncoder encoder;

    private CRUDManager manager;
    private System system;

    private UserService userService;
    private SystemService systemService;
    private EmailService emailService;

    private RegisterService() {
    }

    public RegisterService(CRUDManager manager, System system) throws Exception {
        this.manager = manager;
        if (system == null)
            throw new Exception("Current System not Informed!");
        this.system = system;

        this.initializer();
    }

    private void initializer() throws Exception {
        this.userService = new UserService(this.manager);
        this.systemService = new SystemService(this.manager);
        this.emailService = new EmailService(null, ConnectionProvider.valueOf(this.system.getConnectionProvider()));
    }

    public void initializer(CRUDManager manager, System system) throws Exception {
        this.manager = manager;
        if (system == null)
            throw new Exception("Current System not Informed!");
        this.system = system;

        this.initializer();
    }

    public void initializer(System system) throws Exception {
        if (system == null)
            throw new Exception("Current System not Informed!");
        this.system = system;

        this.emailService = new EmailService(null, ConnectionProvider.valueOf(this.system.getConnectionProvider()));
    }

    public void register(User user) throws Exception {
        try {
            User systemUser = this.userService.findByLogin(user.getLogin().getFirst(), user.getOauthProvider(), Boolean.TRUE);
            if (systemUser != null) {
                if (this.systemService.userHasAccessToSystem(user, this.system)) {
                    throw new Exception("There is already an user with this login!");
                }

                systemUser.getSystems().add(this.system);
                systemUser.markAsModified();
                systemUser.addModifiedAttributes("system");
            } else {
                systemUser = new User();
                systemUser.markAsNew();

                UUID id = UUID.randomUUID();

                systemUser.setId(id.toString());
                systemUser.setLogin(user.getLogin());
                systemUser.setOauthProvider("GOOGLE");
                systemUser.setPassword(this.encoder.encode(user.getPassword()));

                Person person = new Person();
                person.setUserId(id);
                person.setName(user.getPerson().getName());
                person.setNickname(user.getPerson().getName().split(" ")[0]);

                systemUser.setPerson(person);
                systemUser.setSystems(Collections.singletonList(this.system));
            }

            this.emailService.sendConfirmEmail(systemUser.toRecord());
        } catch (Exception ex) {
            if (!this.manager.isClosed() && this.manager.inTransaction())
                this.manager.revertTransaction();

            throw ex;
        }
    }

    public String confirm(String id) throws Exception {
        return this.emailService.confirm(id);
    }
}
