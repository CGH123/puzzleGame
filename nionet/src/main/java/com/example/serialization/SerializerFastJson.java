package com.example.serialization;

import com.alibaba.fastjson.JSON;

public class SerializerFastJson implements Serializer{
    @Override
    public <T> T deserialize(byte[] inputBytes, Class<T> paramCls) {
        return JSON.parseObject(inputBytes, paramCls);
    }

    @Override
    public <T> byte[] serialize(T paramObject) {
        return JSON.toJSONBytes(paramObject);
    }


}
