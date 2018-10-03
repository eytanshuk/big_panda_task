package com.eyt.play.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class SimpleConcurrentMapStatusRepo implements StatusRepository{

    private static final String LINE_END = System.lineSeparator();

    private ConcurrentHashMap<String, LongAdder> wordCounterMap;
    private ConcurrentHashMap<String, LongAdder> eventTypeCountersMap;

    public SimpleConcurrentMapStatusRepo() {
        wordCounterMap = new ConcurrentHashMap<>();
        eventTypeCountersMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addWord(String word) {
        incrementCounter(word, wordCounterMap);
    }

    @Override
    public void addEventType(String eventTypeVal) {
        incrementCounter(eventTypeVal, eventTypeCountersMap);
    }

    @Override
    public String getCountersDisplayStatus() {
        StringBuilder resultBuilder = new StringBuilder("Event type counters").append(LINE_END);
        eventTypeCountersMap.forEach((key, value) -> resultBuilder.append(key).
                append(" counter value: ").append(value).append(LINE_END));

        resultBuilder.append("Word appearance counters").append(LINE_END);
        wordCounterMap.forEach((key, value) -> resultBuilder.append(key).
                append(" counter value: ").append(value).append(LINE_END));

        return resultBuilder.toString();
    }

    private static void incrementCounter(String word, Map<String, LongAdder> countersMap) {
        //they may be some locking, especially word insert in the first time
        //can be improved using better in memory repo like Redis.
        LongAdder wordConter = countersMap.get(word);
        if(wordConter != null ){
            wordConter.increment();
        } else {
            LongAdder newCounter = new LongAdder();
            newCounter.increment();
            LongAdder counterExist = countersMap.putIfAbsent(word, newCounter);
            if(counterExist != null){
                //someone set new counter before current thread.
                counterExist.increment();
            }
        }
    }
}
