package org.project.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.Charset;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        System.out.println(Charset.defaultCharset().displayName());
        SpringApplication.run(ServerApplication.class, args);
    }
}
