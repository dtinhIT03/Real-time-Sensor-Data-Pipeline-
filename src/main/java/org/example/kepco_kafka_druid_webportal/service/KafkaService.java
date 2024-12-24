package org.example.kepco_kafka_druid_webportal.service;

import org.example.kepco_kafka_druid_webportal.entity.Data_sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class KafkaService {

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;
    //hàm tạo dữ liệu
    public List<Data_sensor> generate(){
        Random random = new Random();
        List<Data_sensor> data = new ArrayList<>();
        List<String> sensorType = List.of("Temperature ","Pressure ","Current ","Airflow ","Flame ");
        for(String sensor : sensorType){
            String genID = String.valueOf(UUID.randomUUID());
            String tagID = "mahoa";
            String value = String.valueOf(random.nextInt(9999));
            String stdID= generateStdId();
            LocalDateTime orgTime = LocalDateTime.now();
            LocalDateTime colTime = orgTime.plusSeconds(2);
            data.add(new Data_sensor(genID,tagID,orgTime,colTime,value,stdID,sensor));
        }
        return data;
    }
    //lập lịch gửi dữ liệu sau mỗi 5s
    @Scheduled(fixedRate = 3000)
    public void sendData(){
        List<Data_sensor> dataSensors =generate();
        for (Data_sensor data : dataSensors){
            kafkaTemplate.send("sensor",data);
        }
    }
    public String generateStdId(){
        String[] businessUnits = {"AA10","AA11","AA12"};//nhà máy
        String[] systems ={"BA","BB","BC","BD"};//hệ thống
        String[] facilities={"CDA","CDB","BLR","TBN"};//thiết bị cảm biến
        String[] additionalAttributes={"FGA","FGB","WTR","OIL"};//đo cái gì
        String[] measuringInstrumentTypes={"HA","HB","PT","FT","TT"};//loại dụng cụ đo
        Random random = new Random();
        // Chọn ngẫu nhiên từ các danh sách
        String serialNumber =String.valueOf(1000+random.nextInt(1000)) ;
        String businessUnit = businessUnits[random.nextInt(businessUnits.length)];
        String system = systems[random.nextInt(systems.length)];
        String facility = facilities[random.nextInt(facilities.length)];
        String additionalAttribute = additionalAttributes[random.nextInt(additionalAttributes.length)];
        String instrumentType = measuringInstrumentTypes[random.nextInt(measuringInstrumentTypes.length)];
        return String.format("%s%s %s %s %s %s", businessUnit, system, facility, additionalAttribute, instrumentType, serialNumber);
    }
}
