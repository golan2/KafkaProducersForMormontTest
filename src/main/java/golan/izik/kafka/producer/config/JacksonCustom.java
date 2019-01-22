package golan.izik.kafka.producer.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class JacksonCustom {

    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());


    public static class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
        public ZonedDateTimeDeserializer() {
            super((Class<?>)null);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
            String date = jsonparser.getText();
            return ZonedDateTime.parse(date, formatter);
        }
    }

    public static class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {
        public ZonedDateTimeSerializer() {
            super((Class<ZonedDateTime>)null);
        }

        @Override
        public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
            gen.writeString(formatter.format(value));
        }
    }
}

