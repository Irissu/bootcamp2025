package com.example.catalog.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddPortsConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatReactiveWebServerFactory> additonalPortCustomizer() {
     return factory -> {
         factory.addAdditionalTomcatConnectors(createConnector(8081));
         factory.addAdditionalTomcatConnectors(createConnector(8082));
     };
    }

    private Connector createConnector(int port) {
        Connector connector = new Connector(TomcatReactiveWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(port);
        return connector;
    }

}
