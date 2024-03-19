package com.w1zer;

import com.w1zer.entity.*;
import com.w1zer.payload.RegisterRequest;
import com.w1zer.repository.ProfileRepository;
import com.w1zer.repository.QuoteRepository;
import com.w1zer.service.AuthService;
import com.w1zer.service.QuoteService;
import com.w1zer.service.QuoteStatusService;
import com.w1zer.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
