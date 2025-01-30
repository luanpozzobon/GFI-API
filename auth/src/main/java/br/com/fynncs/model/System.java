package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;

import java.util.UUID;

public class System extends ModelState<System> {
    private UUID id;
    private String name;
    private String database;
    private String connectionProvider;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getConnectionProvider() {
        return connectionProvider;
    }

    public void setConnectionProvider(String connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
}
