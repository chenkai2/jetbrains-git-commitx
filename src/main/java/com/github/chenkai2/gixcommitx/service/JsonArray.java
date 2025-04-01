package com.github.chenkai2.gixcommitx.service;

import com.google.gson.JsonElement;

/**
 * 简单的JsonArray包装类，用于构建JSON数组
 */
public class JsonArray {
    private final com.google.gson.JsonArray jsonArray;

    public JsonArray() {
        this.jsonArray = new com.google.gson.JsonArray();
    }

    public void add(JsonElement element) {
        jsonArray.add(element);
    }

    public com.google.gson.JsonArray getJsonArray() {
        return jsonArray;
    }
}