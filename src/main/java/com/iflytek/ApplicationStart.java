package com.iflytek;import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.context.annotation.ImportResource;/** * @author: demopoo * @Date: Created in 下午4:14 2018/4/16 * @Des: * @Modifyed By: */@ImportResource(locations = {"classpath:/hbase-spring.xml"})@SpringBootApplicationpublic class ApplicationStart {    public static void main(String[] args) {        SpringApplication.run(ApplicationStart.class,args);    }}