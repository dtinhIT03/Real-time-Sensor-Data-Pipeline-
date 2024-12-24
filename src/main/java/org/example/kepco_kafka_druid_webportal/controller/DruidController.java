package org.example.kepco_kafka_druid_webportal.controller;

import org.example.kepco_kafka_druid_webportal.DruidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DruidController {
    @Autowired
    DruidService druidService;
    @PostMapping("/")
    public ResponseEntity<String> query(@RequestBody Object sql){
        return  druidService.executeQuery(sql);
    }


    @MessageMapping("/getdataSensor")
    @SendTo("/sensor")
    public ResponseEntity<String> dataSensor(){
        return druidService.getData();
    }
}
