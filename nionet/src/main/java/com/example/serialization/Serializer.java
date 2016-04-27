package com.example.serialization;


public interface Serializer {
    <T> T parse(String jsonString, Class<T> paramCls);
    String serialize(Object paramObject);
}
