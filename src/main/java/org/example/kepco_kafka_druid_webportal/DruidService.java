package org.example.kepco_kafka_druid_webportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//{ \"query\": \"SELECT * FROM wikipedia WHERE user='BlueMoon2662'\", \"context\": {\"sqlQueryId\": \"request01\"}, \"header\": true, \"typesHeader\": true, \"sqlTypesHeader\": true }
@Service
public class DruidService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private String url="http://172.22.23.14:8888/druid/v2/sql";
    //hàm query tới druid
    public ResponseEntity<String> getData(){
        // tạo đối tượng thực hiện yêu cầu HTTP
        RestTemplate restTemplate = new RestTemplate();
        // tạo đối tượng để chứa thông tin header của yêu cầu HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\n" +
                "    \"query\": \"SELECT * FROM \\\"sensor_data\\\" " +
//                "WHERE \\\"sensor_type\\\" = 'Flame ' " +
                "ORDER BY \\\"__time\\\" DESC\",\n" +
                "    \"context\": {\n" +
                "        \"sqlQueryId\": \"request01\"\n" +
                "    }\n" +
                "}";
        // tạo HttpEntity với body và header
        HttpEntity<Object> entity = new HttpEntity<>(json, headers);
        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            return ResponseEntity.status(500).body("Lỗi khi gửi yêu cầu đến Druid: " + e.getMessage());
        }
    }
    //query druid và push data định kỳ tới websocket
//    @Scheduled(fixedRate = 3000)
    public void pushWebsocket(){
        String data = getData().getBody();
        System.out.println(data);
        messagingTemplate.convertAndSend("/sensor",data);
    }

    public ResponseEntity<String> executeQuery(Object sql){
        // tạo đối tượng thực hiện yêu cầu HTTP
        RestTemplate restTemplate = new RestTemplate();
        // tạo đối tượng để chứa thông tin header của yêu cầu HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // tạo HttpEntity với body và header
        HttpEntity<Object> entity = new HttpEntity<>(sql, headers);

        try {
            return restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            return ResponseEntity.status(500).body("Lỗi khi gửi yêu cầu đến Druid: " + e.getMessage());
        }
    }

}
