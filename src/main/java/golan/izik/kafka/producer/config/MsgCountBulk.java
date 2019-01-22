package golan.izik.kafka.producer.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Comparator;

@Data
public class MsgCountBulk {

    static final Comparator<MsgCountBulk> DATE_PERIOD_COMPARATOR = new DatePeriodComparator();

    @JsonSerialize(using = JacksonCustom.ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = JacksonCustom.ZonedDateTimeDeserializer.class)
    private ZonedDateTime from;
    @JsonSerialize(using = JacksonCustom.ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = JacksonCustom.ZonedDateTimeDeserializer.class)
    private ZonedDateTime to;
    private int messages;     //per device for all period
    private int devices;
    private String devicePrefix;

    private static class DatePeriodComparator implements Comparator<MsgCountBulk> {
        @Override
        public int compare(MsgCountBulk o1, MsgCountBulk o2) {
            if (o1.from.equals(o2.from))
                return o1.to.compareTo(o2.to);
            else
                return o1.from.compareTo(o2.from);
        }
    }


}
