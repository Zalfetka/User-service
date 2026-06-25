package com.example.food.service;

import io.camunda.zeebe.client.ZeebeClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//@Component
//@Profile("!test")
//public class MyWorker {
//
//    private final ZeebeClient client;
//
//    public MyWorker(ZeebeClient client) {
//        this.client = client;
//    }
//
//    @PostConstruct
//    public void startWorker() {
//        client.newWorker()
//                .jobType("my-worker")
//                .handler((jobClient, job) -> {
//                    System.out.println("Выполняю задачу: " + job.getKey());
//
//                    jobClient.newCompleteCommand(job.getKey())
//                            .send()
//                            .join();
//                })
//                .open();
//    }
//}
