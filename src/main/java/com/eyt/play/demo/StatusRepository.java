package com.eyt.play.demo;

public interface StatusRepository {
    void addWord(String woed);
    void addEventType(String eventTypeVal);
    String getCountersDisplayStatus();
}
