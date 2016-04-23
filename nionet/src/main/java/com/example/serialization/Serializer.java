package com.example.serialization;

import com.example.protocol.Entity;
import com.example.protocol.MSGProtocol;


public interface Serializer {
    <T extends Entity> MSGProtocol<T> parse(String jsonString, Class<T> paramCls);
    String serialize(Object paramObject);
}
