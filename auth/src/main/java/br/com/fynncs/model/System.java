package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        System system = (System) o;
        return Objects.equals(getId(), system.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public br.com.fynncs.record.System toRecord() {
        return new br.com.fynncs.record.System(
                this.getId(),
                this.getName(),
                this.getDatabase(),
                this.getConnectionProvider()
        );
    }
}
