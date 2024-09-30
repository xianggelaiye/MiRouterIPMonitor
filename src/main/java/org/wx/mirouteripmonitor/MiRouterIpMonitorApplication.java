package org.wx.mirouteripmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class MiRouterIpMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiRouterIpMonitorApplication.class, args);
    }

}
