package com.example.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.protocol.Entity;
import com.example.protocol.MSGProtocol;

public class SerializerFastJson implements Serializer {
    private SerializerFastJson() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    public static Serializer getInstance() {
        return SingletonHolder.serializer;
    }

    @Override
    public <T extends Entity> MSGProtocol<T> parseObject(String jsonString, Class<T> paramCls) {
        return JSON.parseObject(jsonString, new TypeReference<MSGProtocol<T>>(paramCls) {
        });
    }

    @Override
    public <T> T parseNull(String jsonString, Class<T> paramCls) {
        return JSON.parseObject(jsonString, paramCls);
    }

    @Override
    public String serialize(Object paramObject) {
        return JSON.toJSONString(paramObject);
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static Serializer serializer = new SerializerFastJson();
    }
}
