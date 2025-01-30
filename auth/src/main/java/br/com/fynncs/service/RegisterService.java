package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.User;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.IUser;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RegisterService {
    private CRUDManager manager;
    private UserService userService;

    private RegisterService() { }

    public RegisterService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userService = new UserService(this.manager);
    }

    public void register(User user) throws Exception {
        try {
            this.manager.beginRequest();

            int lines = this.userService.persist(user);
        } catch (Exception ex) {
            if (!this.manager.isClosed() && this.manager.inTransaction())
                this.manager.revertTransaction();

            throw ex;
        }

    }
}
