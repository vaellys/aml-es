package com.ist.common.es.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {
    private JsonUtils(){
    }

    /**
     * json字符串转化类
     * for render to view!
     * 
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        SerializeConfig config = new SerializeConfig();
        // 指定输出日期的格式，只显示年月日
        config.put(Date.class, new ObjectSerializer() {

            @Override
            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
                serializer.write(DateUtils.format((Date) object));
            }
        });
        // 指定输出日期的格式，只显示输出时分秒：
        config.put(Time.class, new ObjectSerializer() {

            @Override
            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
                serializer.write(DateUtils.format((Time) object));
            }
        });
        // 指定输出日期的格式，比如修改为输出毫秒：
        config.put(Timestamp.class, new ObjectSerializer() {

            @Override
            public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
                serializer.write(DateUtils.format((Timestamp) object));
            }
        });
        // list字段如果为null，输出为[]，而不是null, 字符类型字段如果为null，输出为""，而不是null
        return JSON.toJSONString(object, config, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 支持序列化时写入类型信息，从而使得反序列化时不至于类型信息丢失。
     * 
     * @param object
     * @return
     */
    public static String encode(Object object) {
        if (null == object) {
            return null;
        }
        return JSON.toJSONString(object, SerializerFeature.WriteClassName);
    }

    /**
     * 解析成FASTJSON对象
     * 
     * @param json
     * @return
     */
    public static Object decode(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parse(json);
    }

    /**
     * 
     * @param str
     *            json字符串
     * @param type
     *            要转换的类，如果该类没有无参数的构造方法，转换时会出错
     * @return
     */
    public static <T> T jsonToObject(String str, Class<T> type) {

        if (str == null) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 解决json串里的属性，在object里mapping不上的时候会抛异常的问题
            mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 解决json串力的属性，没有用双引号，会抛异常的问题
            mapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            return mapper.readValue(str, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize " + str + " using jackson, cause: " + e.getMessage(), e);
        }
    }
}
