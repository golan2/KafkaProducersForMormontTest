package golan.izik.kafka.producer.config;

import lombok.Data;

import java.util.List;

@Data
public class MsgCountJob {
    private List<MsgCountBulk> bulks;
}