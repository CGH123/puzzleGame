package com.example.serialization;

import com.example.protocol.Entity;
import com.example.protocol.MSGProtocol;


public interface Serializer {
    <T extends Entity> MSGProtocol<T> parseObject(String jsonString, Class<T> paramCls);
    <T> T parseNull(String jsonString, Class<T> paramCls);
    String serialize(Object paramObject);
}
