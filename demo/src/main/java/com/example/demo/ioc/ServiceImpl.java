package com.example.catalog.ioc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements com.example.catalog.ioc.Service {
    private Repository repository;

 public ServiceImpl(/*@Qualifier("verdad")*/ Repository repository) {
     this.repository = repository;
 }

    @Override
    public void guardar() {
        // ...
        repository.guardar();
    }
}
