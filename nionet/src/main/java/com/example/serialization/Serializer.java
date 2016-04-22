package com.example.serialization;


public interface Serializer {
    <T> T deserialize(byte[] inputBytes, Class<T> paramCls);
    <T> byte[] serialize(T paramObject);
}
