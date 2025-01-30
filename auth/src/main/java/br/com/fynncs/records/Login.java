package br.com.fynncs.records;

public record Login(
        String login,
        String oauthProvider,
        String password
) { }