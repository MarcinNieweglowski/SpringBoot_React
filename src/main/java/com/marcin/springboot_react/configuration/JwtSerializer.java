package com.marcin.springboot_react.configuration;

import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;
import org.springframework.stereotype.Component;

@Component
public class JwtSerializer implements Serializer {

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return new byte[0];
    }
}
