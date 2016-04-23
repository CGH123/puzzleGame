package com.example.serialization;

import com.alibaba.fastjson.JSON;


/**
 * 序列化实现
 */
public class SerializerFastJson implements Serializer{

    /**
     * 反序列化
     * @param inputBytes
     * @param paramCls
     * @param <T>
     * @return
     */
    @Override
    public  <T> T deserialize(byte[] inputBytes, Class<T> paramCls) {
        T t = JSON.parseObject(inputBytes, paramCls);
        return t;
    }

    /**
     * 序列化
     * @param paramObject
     * @param <T>
     * @return
     */
    @Override
    public <T> byte[] serialize(T paramObject) {
        return JSON.toJSONBytes(paramObject);
    }


}
