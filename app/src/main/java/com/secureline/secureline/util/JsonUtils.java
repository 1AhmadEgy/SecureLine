package com.secureline.secureline.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        if (json == null) return map;

        java.util.Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = json.get(key);
                if (value instanceof JSONObject) {
                    map.put(key, jsonToMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    map.put(key, jsonToList((JSONArray) value));
                } else {
                    map.put(key, value);
                }
            } catch (Exception e) {
                // Skip
            }
        }
        return map;
    }

    public static List<Object> jsonToList(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        if (jsonArray == null) return list;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object value = jsonArray.get(i);
                if (value instanceof JSONObject) {
                    list.add(jsonToMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    list.add(jsonToList((JSONArray) value));
                } else {
                    list.add(value);
                }
            } catch (Exception e) {
                // Skip
            }
        }
        return list;
    }
}
