package com.example.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

public class SerializerFastJson implements Serializer {
    private SerializerFastJson(){
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static Serializer serializer = new SerializerFastJson();
    }

    public static Serializer getInstance() {
        return SingletonHolder.serializer;
    }

    @Override
    public <T> T parseObject(String jsonString, Class<T> paramCls) {
        return JSON.parseObject(jsonString, paramCls);
    }

    @Override
    public <T> List<T> parseArray(String jsonString, Class<T> paramCls) {
        return JSON.parseArray(jsonString, paramCls);
    }

    @Override
    public String serialize(Object paramObject) {
        return JSON.toJSONString(paramObject);
    }
}
