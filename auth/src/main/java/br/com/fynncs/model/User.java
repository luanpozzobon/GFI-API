package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;
import br.com.fynncs.core.annotation.RegisterAttributeModified;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.util.List;
import java.util.UUID;

@Table(name = "user", schema = "nentech")
public class User extends ModelState<User> {
    @Column(name = "id")
    private String id;

    @Column(name = "login")
    private List<String> login;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "password")
    private String password;

    private Person person;
    private List<System> systems;

    public String getId() {
        return id;
    }

    @RegisterAttributeModified("id")
    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLogin() {
        return login;
    }

    @RegisterAttributeModified("login")
    public void setLogin(List<String> login) {
        this.login = login;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    @RegisterAttributeModified("oauth_provider")
    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getPassword() {
        return password;
    }

    @RegisterAttributeModified("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public Person getPerson() {
        return person;
    }

    @RegisterAttributeModified("person")
    public void setPerson(Person person) {
        this.person = person;
    }

    public List<System> getSystems() {
        return systems;
    }

    @RegisterAttributeModified("systems")
    public void setSystems(List<System> systems) {
        this.systems = systems;
    }

    public br.com.fynncs.record.User toRecord() {
        return new br.com.fynncs.record.User(
                UUID.fromString(this.getId()),
                this.getLogin(),
                this.getOauthProvider(),
                this.getPassword(),
                this.getPerson().toRecord(),
                this.getSystems().parallelStream().map(System::toRecord).toList()
        );
    }
}