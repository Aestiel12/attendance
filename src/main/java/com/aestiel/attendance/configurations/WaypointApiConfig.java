package com.aestiel.attendance.configurations;

import com.aestiel.attendance.proxies.WaypointApi;
import com.aestiel.attendance.proxies.WaypointServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WaypointApiConfig {

    @Value("${waypoint.username}")
    public String username;

    @Value("${waypoint.password}")
    public String password;
    @Bean
    public WaypointApi createWaypointApi(){
        return WaypointServiceFactory.createService(WaypointApi.class, username, password);
    }
}
