package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.User;
import org.springframework.stereotype.Service;

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

    public boolean register(User user) throws Exception {
        try {
            if (this.userService.findByLogin(user.getLogin().getFirst(), user.getOauthProvider(), Boolean.FALSE) != null) {
                return Boolean.FALSE;
            }
            this.manager.beginRequest();

            int lines = this.userService.persist(user);

            return lines > 0;
        } catch (Exception ex) {
            if (!this.manager.isClosed() && this.manager.inTransaction())
                this.manager.revertTransaction();

            throw ex;
        }

    }
}
