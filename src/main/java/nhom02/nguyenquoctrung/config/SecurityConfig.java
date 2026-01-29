package nhom02.nguyenquoctrung.config;

import nhom02.nguyenquoctrung.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/",
                        "/register", "/error")
                .permitAll()

                .requestMatchers("/books/edit",

                        "/books/delete")
                .authenticated()

                .requestMatchers("/books", "/books/add")

                .authenticated()

                .requestMatchers("/api/**")

                .authenticated()

                .anyRequest().authenticated()

        )
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll())
                .formLogin(formLogin -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("hutech").rememberMeCookieName("hutech")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(userDetailsService()))
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/403"))
                .sessionManagement(sessionManagement -> sessionManagement.maximumSessions(1)
                        .expiredUrl("/login"))
                .httpBasic(httpBasic -> httpBasic.realmName("hutech"))
                .build();
    }
}
