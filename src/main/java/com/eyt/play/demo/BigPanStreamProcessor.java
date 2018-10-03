package com.eyt.play.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.BaseStream;

public class BigPanStreamProcessor {

    private static final String EVENT_TYPE = "event_type";

    private StatusRepository repo;

    BigPanStreamProcessor(StatusRepository repo) {
        this.repo = repo;
    }

    public void processStream(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        Flux<String> fluxUsing = Flux.using(br::lines,
                Flux::fromStream,
                BaseStream::close);

        new Thread(()-> {
            fluxUsing.filter(line-> line.contains(EVENT_TYPE)).subscribe(this::processLine);
        }).start();

        System.out.println("End of process stream method actual processing on another htread");
    }


    private void processLine(String line){
        try {
            JsonParser parser = new JsonFactory().createParser(line);
            parser.nextToken();
            String eventType = parser.nextFieldName();
            if(EVENT_TYPE.equals(eventType)){
                String eventTypeStr = parser.nextTextValue();
                if(eventTypeStr != null){
                    repo.addEventType(eventTypeStr);
                    //continue for data filed
                    String datafield = parser.nextFieldName();
                    if(datafield.equals("data")) {
                        String datVal = parser.nextTextValue();
                        repo.addWord(datVal);
                    }
                }
            }
        } catch (IOException e) {
            //unexpected , ignore
            System.out.println("IOException occurred " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getDisplayStatusString(){
        return repo.getCountersDisplayStatus();
    }
}
