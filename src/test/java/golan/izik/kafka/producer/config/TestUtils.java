package golan.izik.kafka.producer.config;

import java.time.ZonedDateTime;

class TestUtils {
    @SuppressWarnings("SameParameterValue")
    static MsgCountBulk bulk(ZonedDateTime from, ZonedDateTime to, int messages, int devices) {
        final MsgCountBulk res = new MsgCountBulk();
        res.setFrom(from);
        res.setTo(to);
        res.setMessages(messages);
        res.setDevices(devices);
        res.setDevicePrefix("device_");
        return res;
    }

    static ZonedDateTime parseZDT(String s) {
        return ZonedDateTime.from(ZonedDateTime.parse(s, JacksonCustom.formatter));
    }
}
