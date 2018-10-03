package com.eyt.play.demo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class StatusController {
    private static final String EXTERNAL_PROCESS_DIR = "C:\\apps\\big_panda_task\\";

    private BigPanStreamProcessor counterProcessor;

    @RequestMapping("/")
    public String index() {
        return "Big Panda Task";
    }

    @RequestMapping("/displayStats")
    public String getCurrentStatus() {
        return counterProcessor.getDisplayStatusString();
    }

    /**
     * Running local process at the given path and start processing process output.
     * Note:No validation will be done here, wil try to run process even if already running.
     * Any call will create a new repository
     * @param pName path to process to listen and process output
     * @throws IOException in case could not start process at the local given path
     */
    @RequestMapping("/startProcessing/{processName}")
    public void startProcessing(@PathVariable("processName") String pName ) throws IOException {
        Process p = Runtime.getRuntime().exec(EXTERNAL_PROCESS_DIR + pName + ".exe");

        //todo consider handling resources using spring
        //creating new processor with new repo todo limited amount of process and prevent duplication
        counterProcessor = new BigPanStreamProcessor(new SimpleConcurrentMapStatusRepo());
        counterProcessor.processStream(p.getInputStream()) ;
    }


}
