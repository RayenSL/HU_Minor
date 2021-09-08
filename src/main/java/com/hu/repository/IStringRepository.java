package com.hu.repository;

public interface IStringRepository {

    public String findByKey(String text);

    public void addString(String text, String count);
}
