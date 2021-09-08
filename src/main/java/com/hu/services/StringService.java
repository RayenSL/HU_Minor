package com.hu.services;

import com.hu.repository.StringRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class StringService {

    public StringService() {
    }

    @Autowired
    StringRepository stringRepository;

    public String toCaps(String word) {
        return word.toUpperCase();
    }

    public String invertWord(String word) {
        byte[] array = word.getBytes();
        byte[] result = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = array[array.length - i - 1];
        }

        return new String(result);
    }

    public String countWords(String text) {
        if (stringRepository.findByKey(text) != null) {
            return stringRepository.findByKey(text);
        } else {
            String[] words = text.split("\\s+");
            String result = Integer.toString(words.length);

            stringRepository.addString(text, result);

            return result;
        }
    }
}
