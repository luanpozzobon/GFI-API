package br.com.fynncs.email_service.record;

import java.util.Date;

public record User(String id, String email, String name,
                   String provider, String system, Date dateRegister) {
}
