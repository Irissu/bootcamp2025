package com.example.catalog.ioc;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("rango")
public class Rango {
    private int min;
    private int max;
}
