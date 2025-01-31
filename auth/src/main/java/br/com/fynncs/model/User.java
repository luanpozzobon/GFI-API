package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;
import br.com.fynncs.core.annotation.RegisterAttributeModified;

import java.util.List;
import java.util.UUID;

public class User extends ModelState<User> {
    private UUID id;
    private List<String> login;
    private String oauthProvider;
    private String password;
    private Person person;
    private List<System> systems;

    public UUID getId() {
        return id;
    }

    @RegisterAttributeModified("id")
    public void setId(UUID id) {
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

    @RegisterAttributeModified("system")
    public void setSystems(List<System> systems) {
        this.systems = systems;
    }
}