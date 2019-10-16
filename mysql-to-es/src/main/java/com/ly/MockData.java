package com.ly;

import java.util.HashMap;
import java.util.Map;

public class MockData {

    public static Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "aa");
        map.put("age", 18);
        map.put("tag", "N/A");

        return map;
    }
}
