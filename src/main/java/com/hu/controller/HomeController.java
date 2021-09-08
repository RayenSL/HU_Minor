package com.hu.controller;

import com.hu.services.StringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class HomeController {

    @Autowired
    StringService stringService;

    @PostMapping("/invert")
    public ResponseEntity<String> invertString(@RequestBody String word) {
        String reverseWord = stringService.invertWord(word);

        return new ResponseEntity<String>(reverseWord, HttpStatus.OK);
    }

    @PostMapping("/count")
    public ResponseEntity<String> countWords(@RequestBody String word) {
        return new ResponseEntity<String>(stringService.countWords(word), HttpStatus.OK);
    }
}
