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

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthService authService,
            RoleService roleService,
            QuoteRepository quoteRepository,
            ProfileRepository profileRepository,
            QuoteStatusService quoteStatusService
    ) {
        return args -> {
//            Role user = roleService.findByName(RoleName.ROLE_USER);
//            Role moderator = roleService.findByName(RoleName.ROLE_MODERATOR);
//            Role admin = roleService.findByName(RoleName.ROLE_ADMIN);
//
//            var adm = new RegisterRequest("admin@admin.ru", "admin", "admin",
//                    Set.of(user, moderator, admin));
//            authService.register(adm);
//
//            var mod = new RegisterRequest("moder@moder.ru", "moder", "moder",
//                    Set.of(user, moderator));
//            authService.register(mod);
//
//            var usr = new RegisterRequest("user@user.ru", "user", "user1",
//                    Set.of(user));
//            authService.register(usr);
//
//            Profile profile = profileRepository.findByLogin("admin").orElseThrow();
//            var quote = new Quote(1L, "abc", null, profile,
//                    quoteStatusService.findByName(QuoteStatusName.PRIVATE));
//            quoteRepository.save(quote);
        };
    }
}
