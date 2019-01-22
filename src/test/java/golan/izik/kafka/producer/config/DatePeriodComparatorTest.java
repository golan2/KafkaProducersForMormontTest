package golan.izik.kafka.producer.config;

import org.junit.Test;

import static golan.izik.kafka.producer.config.MsgCountBulk.DATE_PERIOD_COMPARATOR;
import static org.junit.Assert.*;

public class DatePeriodComparatorTest {

    @Test
    public void whenFromIsNotTheSame_compareOnlyAccordingToFrom() {
        MsgCountBulk before = new MsgCountBulk();
        before.setFrom(TestUtils.parseZDT("2018-01-01 12:00:00"));
        before.setTo(TestUtils.parseZDT("2018-01-01 15:00:00"));
        MsgCountBulk after = new MsgCountBulk();
        after.setFrom(TestUtils.parseZDT("2018-01-01 12:00:01"));
        after.setTo(TestUtils.parseZDT("2018-01-01 14:00:00"));
        assertEquals("Bulks with different from to should be compared by from", DATE_PERIOD_COMPARATOR.compare(after, before), 1);
    }

    @Test
    public void whenFromIsTheSame_compareAccordingToPeriodLength() {
        MsgCountBulk before = new MsgCountBulk();
        before.setFrom(TestUtils.parseZDT("2018-01-01 12:00:00"));
        before.setTo(TestUtils.parseZDT("2018-01-01 13:00:00"));
        MsgCountBulk after = new MsgCountBulk();
        after.setFrom(TestUtils.parseZDT("2018-01-01 12:00:00"));
        after.setTo(TestUtils.parseZDT("2018-01-01 14:00:00"));
        assertEquals("Bulks with different from to should be compared by from", DATE_PERIOD_COMPARATOR.compare(after, before), 1);
    }

}