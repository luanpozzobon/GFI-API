package br.com.fynncs.email_service.record;

import java.util.List;
import java.util.UUID;

public record User(UUID id, List<String> login, String oauthProvider,
                   String password, Person person, List<System> systems) {
}
