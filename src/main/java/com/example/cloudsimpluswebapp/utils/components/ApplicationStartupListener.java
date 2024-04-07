package com.example.cloudsimpluswebapp.utils.components;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        String url = "http://localhost:8080"; // Замените на фактический URL вашего приложения
        System.out.println("Приложение запущено. Доступно по адресу: " + url);
    }
}
