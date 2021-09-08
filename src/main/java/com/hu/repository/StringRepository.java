package com.hu.repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class StringRepository implements IStringRepository {

    private Map<String, String> texts;

    public StringRepository() {
        texts = new HashMap<>();
    }

    @Override
    public String findByKey(String text) {
        return texts.get(text);
    }

    @Override
    public void addString(String text, String count) {
        texts.put(text, count);
    }

}
