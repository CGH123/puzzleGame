package com.example.serialization;

import com.alibaba.fastjson.JSON;

public class SerializerFastJson implements Serializer{
    @Override
    public <T> T deserialize(byte[] inputBytes, Class<T> paramCls) {
        T t = JSON.parseObject(inputBytes, paramCls);
        return t;
    }

    @Override
    public <T> byte[] serialize(T paramObject) {
        return JSON.toJSONBytes(paramObject);
    }

}
