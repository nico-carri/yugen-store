package ar.org.centro8curos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/home", "/usuario/registro", "/usuario/login",
                                                                "/css/**", "/js/**", "/img/**")
                                                .permitAll()
                                                .requestMatchers("/productos/nuevo/**", "/productos/editar/**",
                                                                "/productos/eliminar/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers("/usuario/perfil/**", "/pedidos/**")
                                                .hasAnyRole("CLIENTE", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/usuario/login")
                                                .loginProcessingUrl("/usuario/login")
                                                .defaultSuccessUrl("/usuario/perfil", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/usuario/login?logout")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}