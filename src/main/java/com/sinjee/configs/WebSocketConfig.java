package com.sinjee.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 小小极客
 * 时间 2020/2/24 23:04
 * @ClassName WebSocketConfig
 * 描述 WebSocketConfig
 **/
@Configuration
@ComponentScan("com.sinjee.service.impl")
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
