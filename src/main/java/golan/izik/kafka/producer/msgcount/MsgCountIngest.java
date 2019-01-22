package golan.izik.kafka.producer.msgcount;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import golan.izik.kafka.producer.config.MsgCountConfig;
import golan.izik.kafka.producer.config.MsgCountJob;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Produce messages to Kafka to simulate a desired MsgCountPerMinute for each object
 */
class MsgCountIngest {

    private static final Logger logger = LoggerFactory.getLogger(MsgCountIngest.class);

    public static void main(String[] args) throws ParseException, IOException {
        logger.info("Command line arguments: {}", Arrays.toString(args));
        final MsgCountConfig conf = new MsgCountConfig(args);
        logger.info("Configuration: {}", conf);

        final File jobFile = new File(conf.getJobFile());
        final StringBuilder buf = new StringBuilder();
        Scanner input = new Scanner(jobFile);
        while (input.hasNextLine()) {
            buf.append(input.nextLine()).append("\n");
        }
        logger.info("Job File:\n" + buf.toString());


        final MsgCountJob job = createJobFromFile(jobFile);
        logger.info("Job: {}", job);

        job.getBulks().forEach(bulk -> new BulkProducer(conf, bulk).produce());

    }

    private static MsgCountJob createJobFromFile(File file) throws IOException {
        final ObjectMapper oo = new ObjectMapper();
        if (!file.exists()) throw new FileNotFoundException("Can't read job file: " + file.getAbsolutePath());
        return (MsgCountJob) oo.readValue(file, new TypeReference<MsgCountJob>(){});
    }

}
