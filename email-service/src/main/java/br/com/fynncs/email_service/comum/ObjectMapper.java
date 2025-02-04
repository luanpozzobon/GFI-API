package br.com.fynncs.email_service.comum;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class ObjectMapper {

    private static final com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T readValue(String object, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(object, clazz);
    }
}
