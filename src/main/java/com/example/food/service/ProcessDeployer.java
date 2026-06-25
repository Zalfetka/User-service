package com.example.food.service;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//@Component
//@Profile("!test")
//public class ProcessDeployer {
//
//    private final ZeebeClient client;
//
//    public ProcessDeployer(ZeebeClient client) {
//        this.client = client;
//    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void deploy() {
//        try {
//              client.newDeployResourceCommand()
//                    .addResourceFromClasspath("process.bpmn")
//                    .send()
//                    .join();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
