package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.User;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.IUser;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class RegisterService {
    private final CRUDManager manager;
    private IUser userPersist;

    public RegisterService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userPersist = CreatePersist.createUser(this.manager);
    }

    public void register(User user) throws SQLException, IllegalAccessException {
        if (this.userPersist.persist(user)) {
            // TODO - Enviar email OTP
        }
    }
}
