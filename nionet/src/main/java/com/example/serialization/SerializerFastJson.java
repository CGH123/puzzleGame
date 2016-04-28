package com.example.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.protocol.Entity;
import com.example.protocol.MSGProtocol;

import java.util.List;

public class SerializerFastJson implements Serializer {
    private SerializerFastJson() {
    }

    public static Serializer getInstance() {
        return SingletonHolder.serializer;
    }


    @Override
    public <T> T parse(String jsonString, Class<T> paramCls) {
        return JSON.parseObject(jsonString, paramCls);
    }


    @Override
    public String serialize(Object paramObject) {
        return JSON.toJSONString(paramObject, SerializerFeature.WriteClassName);
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static Serializer serializer = new SerializerFastJson();
    }
}
