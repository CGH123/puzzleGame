package com.example.serialization;



import java.util.List;

public interface Serializer {
    Serializer DEFAULT = SerializerFastJson.getInstance();
    <T> T parseObject(String jsonString, Class<T> paramCls);
    <T> List<T> parseArray(String jsonString, Class<T> paramCls);
    String serialize(Object paramObject);
}
