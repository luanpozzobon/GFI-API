package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.System;
import br.com.fynncs.model.User;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.ISystem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SystemService {
    private CRUDManager manager;
    private ISystem systemDAO;

    protected SystemService() {
    }

    public SystemService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.systemDAO = CreatePersist.createSystem(this.manager);
    }

    public System getCurrentSystem(HttpServletRequest request) throws Exception {
        List<System> systems = this.getAll();

        Optional<System> currentSystem = systems.parallelStream()
                .filter(system -> Pattern.compile(Pattern.quote(system.getName()), Pattern.CASE_INSENSITIVE)
                        .matcher(request.getHeader("Referer")).find()).findFirst();

        if (currentSystem.isEmpty())
            throw new Exception("System not found!");

        return currentSystem.get();
    }

    public boolean userHasAccessToSystem(User user, System system) {
        return user.getSystems().parallelStream().anyMatch(system::equals);
    }

    public List<System> findByUser(String userId) throws Exception {
        return this.systemDAO.findByUser(userId);
    }

    public List<System> getAll() throws Exception {
        return this.systemDAO.getAll();
    }

    public int saveUserAccess(User user) throws Exception {
        List<System> systems = this.findByUser(user.getId());

        user.setSystems(user.getSystems().stream().filter(systems::contains).toList());
        int lines = 0;
        for (System sys : user.getSystems()) {
            this.systemDAO.saveUserAccess(user.getId(), sys.getId());
            ++lines;
        }

        return lines;
    }
}
