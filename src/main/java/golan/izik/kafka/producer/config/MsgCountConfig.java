package golan.izik.kafka.producer.config;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Data
@ToString
public class MsgCountConfig {

    private static final String DEFAULT_BOOTSTRAP_SERVERS = "kafka-hs:9100";
    private static final String DEFAULT_TOPIC             = "activity";
    private static final String DEFAULT_ORG               = "org";
    private static final String DEFAULT_PROJ              = "proj";
    private static final String DEFAULT_ENV               = "env";
    private static final String DEFAULT_ENV_UUID          = "6f33d5e8-f410-11e8-b5e9-79dc71f7ba86";

    private final String bootstrapServers;
    private final String topic;
    private final String organization;
    private final String project;
    private final String environment;
    private final String envUuid;
    private final String jobFile;

    public MsgCountConfig(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("b", "bootstrap.servers", true, "Kafka bootstrap servers list");
        options.addOption("t", "topic", true, "Kafka topic to ingest into");
        options.addOption("j", "job.file", true, "Full path to the job json file");
        options.addOption("o", "organization", true, "Organization");
        options.addOption("p", "project", true, "Project");
        options.addOption("e", "environment", true, "Environment");
        options.addOption("eid", "envUuid", true, "Environment UUID");

        CommandLineParser parser = new BasicParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.bootstrapServers = commandLine.getOptionValue("bootstrap.servers", DEFAULT_BOOTSTRAP_SERVERS);
        this.topic = commandLine.getOptionValue("topic", DEFAULT_TOPIC);
        this.jobFile = commandLine.getOptionValue("job.file");
        this.organization = commandLine.getOptionValue("organization", DEFAULT_ORG);
        this.environment = commandLine.getOptionValue("environment", DEFAULT_ENV);
        this.project = commandLine.getOptionValue("project", DEFAULT_PROJ);
        this.envUuid = commandLine.getOptionValue("envUuid", DEFAULT_ENV_UUID);

        if (this.jobFile==null) throw new IllegalArgumentException("Missing mandatory command line argument: job.file");

    }
}
