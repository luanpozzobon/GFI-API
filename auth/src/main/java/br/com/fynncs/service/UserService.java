package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.User;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.IUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private CRUDManager manager;
    private IUser userDAO;
    private SystemService systemService;

    private UserService() { }

    public UserService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userDAO = CreatePersist.createUser(this.manager);

        this.systemService = new SystemService(this.manager);
    }

    private User populate(User user) throws Exception {
        if (user != null) {
            user.setSystems(systemService.findByUser(user.getId()));
        }

        return user;
    }

    public int persist(User user) throws Exception {
        int lines = this.userDAO.persist(user);

        if (lines < 1) throw new Exception("No changes occurred!");

        if (!user.isDeleted()) {
            if (user.isNew() || user.checkerModifiedAttributes("systems")) {
                this.systemService.saveUserAccess(user);
            }
        }

        return lines;
    }

    public User findById(UUID id, boolean populate) throws Exception {
        if (id == null) throw new Exception("Id must not be null!");

        User user = this.userDAO.findById(id);

        return populate ? this.populate(user) : user;
    }

    public User findByLogin(String login, boolean populate) throws Exception {
        return this.findByLogin(login, null, populate);
    }

    public User findByLogin(String login, String oauthProvider, boolean populate) throws Exception {
        if (login == null || login.isBlank()) throw new Exception("Login must be informed!");

        User user = this.userDAO.findByLogin(login, oauthProvider);

        return populate ? this.populate(user) : user;
    }
}
