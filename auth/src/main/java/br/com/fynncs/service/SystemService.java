package br.com.fynncs.service;

import br.com.fynncs.core.connection.CRUDManager;
import br.com.fynncs.model.System;
import br.com.fynncs.persist.CreatePersist;
import br.com.fynncs.persist.ISystem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SystemService {
    private CRUDManager manager;
    private ISystem systemDAO;

    private SystemService() { }

    public SystemService(CRUDManager manager) throws Exception {
        this.manager = manager;
        this.initializer();
    }

    private void initializer() throws Exception {
        this.systemDAO = CreatePersist.createSystem(this.manager);
    }

    public List<System> findByUser(UUID userId) throws Exception {
        return this.systemDAO.findByUser(userId);
    }
}
