package golan.izik.kafka.producer.msgcount;

import golan.izik.kafka.producer.config.MsgCountBulk;
import golan.izik.kafka.producer.config.MsgCountConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

class BulkProducer {
    private static final Logger logger = LoggerFactory.getLogger(BulkProducer.class);

    private static final String RUN_ID = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 999_999));
    private static final DateTimeFormatter DF_MILLISEC = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());


    private final MsgCountBulk bulk;
    private final MsgCountConfig conf;

    BulkProducer(MsgCountConfig conf, MsgCountBulk bulk) {
        this.bulk = bulk;
        this.conf = conf;
    }

    void produce() {
        final long secondsBetweenMessages = Duration.between(bulk.getFrom(), bulk.getTo()).getSeconds() / bulk.getMessages();
        logger.info("secondsBetweenMessages=[{}] bulk=[{}]", secondsBetweenMessages, bulk);

        try (Producer<String, String> producer = new KafkaProducer<>(getKafkaProperties())) {
            ZonedDateTime dt = bulk.getFrom();
            while (dt.isBefore(bulk.getTo())) {
                for (int objectIndex = 0 ; objectIndex<bulk.getDevices() ; objectIndex++) {
                    final String objectId = "" + String.format("%03d", objectIndex);
                    producer.send(new ProducerRecord<>(conf.getTopic(), objectId, message(dt, objectId)));
                }
                dt = dt.plus(secondsBetweenMessages, ChronoUnit.SECONDS);
            }
        }
    }

    private Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", conf.getBootstrapServers());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    private String message(ZonedDateTime timestamp, String objectId) {
        return message(UUID.randomUUID().toString(), timestamp.toInstant().toEpochMilli(), conf.getOrganization(), conf.getProject(), conf.getEnvironment(), conf.getEnvUuid(), objectId, timestamp);

    }

    @SuppressWarnings("SameParameterValue")
    private static String message(String txnId, long timestamp, String organization, String project, String environment, String envUuid, String objectId, ZonedDateTime ts) {
        return String.format(messageTemplate_, RUN_ID+"_"+txnId, timestamp, organization, project, organization, project, environment, envUuid, objectId, ts.format(DF_MILLISEC), RUN_ID);
    }

    private static final String messageTemplate_ = ("" +
            "{\n" +
            "\t\"ing_msg_tnxid\": \"%s\",\n" +                                              //TXN_ID
            "\t\"ing_msg_timestamp\": \"%d\",\n" +                                          //TIMESTAMP
            "\t\"ing_msg_org_bucket\": \"%s\",\n" +                                         //ORG_B
            "\t\"ing_msg_project_bucket\": \"%s\",\n" +                                     //PROJ_B
            "\t\"ing_msg_orgid\": \"%s\",\n" +                                              //ORG
            "\t\"ing_msg_projectid\": \"%s\",\n" +                                          //PROJ
            "\t\"ing_msg_environment\": \"%s\",\n" +                                        //ENV
            "\t\"ing_msg_envuuid\": \"%s\",\n" +                                            //ENV_UUID
            "\t\"ing_msg_device_type\": \"vehicle\",\n" +
            "\t\"ing_msg_deviceId\": \"%s\",\n" +                                           //OBJECT_ID
            "\t\"user_param\": {\n" +
            "\t\t\"humanTimestamp\": {\n" +
            "\t\t\t\"value\": \"%s\",\n" +                                                  //FORMATTED TIMESTAMP
            "\t\t\t\"name\": \"text\",\n" +
            "\t\t\t\"id\": 1\n" +
            "\t\t},\n" +
            "\t\t\"runId\": {\n" +
            "\t\t\t\"value\": %s,\n" +                                                      //RUN ID
            "\t\t\t\"name\": \"number\",\n" +
            "\t\t\t\"id\": 1\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}")
            .replace("\n", "").replace("\t", "");


}
