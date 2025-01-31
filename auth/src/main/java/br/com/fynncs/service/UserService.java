package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.Person;
import br.com.fynncs.model.User;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.IUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private CRUDManager manager;
    private IUser userDAO;
    private PersonService personService;
    private SystemService systemService;

    private UserService() { }

    public UserService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.userDAO = CreatePersist.createUser(this.manager);

        this.personService = new PersonService(this.manager);
        this.systemService = new SystemService(this.manager);
    }

    private User populate(User user) throws Exception {
        if (user != null) {
            user.setPerson(personService.findById(user.getId()));
            user.setSystems(systemService.findByUser(user.getId()));
        }

        return user;
    }

    public int persist(User user) throws Exception {
        int lines = this.userDAO.persist(user);

        if (lines < 1) throw new Exception("No changes occurred!");

        Person person;
        if (user.isNew() && user.getPerson() == null) {
            person = new Person();
            person.setUserId(user.getId());
            this.personService.persist(person);
        }

        if (user.isModified() && user.checkerModifiedAttributes("person")) {
            person = user.getPerson();
            this.personService.persist(person);
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

    public User findByLogin(String login, String oauthProvider) throws Exception {
        return this.findByLogin(login, oauthProvider, Boolean.FALSE);
    }

    public User findByLogin(String login, String oauthProvider, boolean populate) throws Exception {
        if (login == null || login.isBlank()) throw new Exception("Login must be informed!");

        User user = this.userDAO.findByLogin(login, oauthProvider);

        return populate ? this.populate(user) : user;
    }
}
