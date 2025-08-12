package com.sohair.journalApp.service;

import com.sohair.journalApp.model.User;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class UserArgumentProvider implements ArgumentsProvider {
    @Override
    public java.util.stream.Stream<? extends org.junit.jupiter.params.provider.Arguments> provideArguments(org.junit.jupiter.api.extension.ExtensionContext context) throws Exception {
        return java.util.stream.Stream.of(
                Arguments.of(User.builder().userName("adnan").password("adnan").build()),
                Arguments.of(User.builder().userName("cool").password("cool").build())
        );
    }
}
