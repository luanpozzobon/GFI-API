package br.com.fynncs.email_service.service.interfaces;

import br.com.fynncs.email_service.model.EmailType;

import java.sql.SQLException;

public interface IEmailTypeAssembly<T> {

    EmailType createEmailType(String type, String enterprise, T value) throws SQLException;

}
