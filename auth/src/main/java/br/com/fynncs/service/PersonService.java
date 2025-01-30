package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.Person;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.IPerson;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PersonService {
    private final CRUDManager manager;
    private IPerson personDAO;

    public PersonService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.personDAO = CreatePersist.createPerson(this.manager);
    }

    public int persist(Person person) throws Exception {
        int lines = this.personDAO.persist(person);

        if (lines < 1) throw new Exception("No changes occurred!");

        return lines;
    }

    public Person findById(UUID id) throws Exception {
        if (id == null) throw new Exception("Id must not be null!");

        return this.personDAO.findById(id);
    }
}
