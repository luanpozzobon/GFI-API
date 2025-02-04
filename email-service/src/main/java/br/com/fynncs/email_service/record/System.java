package br.com.fynncs.email_service.record;

import java.util.UUID;

public record System(UUID id, String name, String database, String connectionProvider) {
}
