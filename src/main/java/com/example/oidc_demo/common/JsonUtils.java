package com.example.oidc_demo.common;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    private final Gson gson = new Gson();

    public String toJson(Object object) {
        return gson.toJson(object);
    }
}
