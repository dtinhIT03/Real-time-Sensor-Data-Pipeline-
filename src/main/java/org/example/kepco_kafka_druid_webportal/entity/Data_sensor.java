package org.example.kepco_kafka_druid_webportal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Data_sensor {
    private String gen_ID;
    private String tag_ID;
    private LocalDateTime org_time;
    private LocalDateTime col_time;
    private String value;
    private String std_ID;
    private String sensor_type;
}
