package com.eyt.play.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		try {
			String line;
			Process p = Runtime.getRuntime().exec
					("c:\\Users\\eytan\\Downloads\\generator-windows-amd64.exe ");
			BufferedReader input =
					new BufferedReader
							(new InputStreamReader(p.getInputStream()));

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            Stream<String> lines = br.lines();
            Flux<String> lineFlux = Flux.fromStream(lines);
            Flux<String> fluxUsing = Flux.using(br::lines,
                    Flux::fromStream,
                    BaseStream::close);

            ConnectableFlux<String> publish = fluxUsing.publish();

            publish.subscribe();
            publish.map(DemoApplicationTests::process1);
            //parralel

            while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();

		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}

    private static String process1(String line) {
        //do nothing
        return line;
    }

    @Test
    public void testEndToEnd() throws IOException, InterruptedException {
	    //connect to process output stream
        //todo take name from parameter
        Process p = Runtime.getRuntime().exec
                ("c:\\apps\\big_panda_task\\generator-windows-amd64.exe ");

        BigPanStreamProcessor statusController = new BigPanStreamProcessor(new SimpleConcurrentMapStatusRepo());
        statusController.processStream(p.getInputStream());

        for (int i = 0;i<10;i++) {
            sleep(2000);

            String result = statusController.getDisplayStatusString();
            assertNotNull(result);
            System.out.println(result);
        }
    }

}
