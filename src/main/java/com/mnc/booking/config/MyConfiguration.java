//package com.mnc.booking.config;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//
//@RequiredArgsConstructor
//@Configuration
//public class MyConfiguration {
//
//  private final ObjectMapper objectMapper;
//
//  @PostConstruct
//  void objectMapperSetup() {
//    this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//  }
//}
