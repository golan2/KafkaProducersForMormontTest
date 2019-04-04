package golan.izik.kafka.producer.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MsgCountJobTest {

    @Test
    public void jacksonTest() throws IOException {
        final MsgCountJob obj1 = createFromScratch();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(obj1));
        final Object obj2 = createFromString(new ObjectMapper().writer().writeValueAsString(obj1));
        assert(obj1.equals(obj2));
    }

    private static Object createFromString(String json) throws IOException {
        final ObjectMapper oo = new ObjectMapper();
        return oo.readValue(json, new TypeReference<MsgCountJob>(){});
    }

    private static MsgCountJob createFromScratch() {
        List<MsgCountBulk> list = new ArrayList<>();
        list.add(TestUtils.bulk(TestUtils.parseZDT("2018-01-01 12:00:00"), TestUtils.parseZDT("2018-01-01 13:00:00"), 10, 12));
        list.add(TestUtils.bulk(TestUtils.parseZDT("2018-01-02 12:00:00"), TestUtils.parseZDT("2018-01-02 13:00:00"), 10, 12));
        list.sort(MsgCountBulk.DATE_PERIOD_COMPARATOR);
        final MsgCountJob res = new MsgCountJob();
        res.setBulks(list);
        return res;
    }


}