package com.orangehi.expo.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by VULCAN on 2018/5/17.
 */

public class JsonUtils {

    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        // 注册日期时间类型反序列化时的适配器(针对反序列化到JavaBean的情况，Map类型不需要处理)
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return stringToDate(json.getAsString());
            }
        });
        gson = builder.create();
    }

    public static final <T> T fromJson(String json, Type type) {
        T list = gson.fromJson(json, type);
        return (T) list;
    }

    public static Date stringToDate(String strDate) {
        Date tmpDate = (new SimpleDateFormat(OHCons.DATATIME)).parse(strDate, new ParsePosition(0));
        if (tmpDate == null) {
            tmpDate = (new SimpleDateFormat(OHCons.DATA)).parse(strDate, new ParsePosition(0));
        }
        return tmpDate;
    }

}
