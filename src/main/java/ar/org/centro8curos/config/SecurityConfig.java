package ar.org.centro8curos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment; // Importante
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays; // Importante

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // Inyectamos Environment para detectar el perfil activo
        private final Environment env;

        public SecurityConfig(Environment env) {
                this.env = env;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                // Detectamos si el perfil "prod" está activo
                boolean isProd = Arrays.asList(env.getActiveProfiles()).contains("prod");

                http
                                .csrf(csrf -> csrf.disable());

                // Lógica de canal dinámico: si es prod, exige HTTPS; si no, exige HTTP (evita
                // redirecciones)
                if (isProd) {
                        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
                } else {
                        http.requiresChannel(channel -> channel.anyRequest().requiresInsecure());
                }

                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico",
                                                                "/assets/**")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/",
                                                                "/index",
                                                                "/nosotros/**",
                                                                "/productos/**",
                                                                "/usuario/login",
                                                                "/usuario/registro",
                                                                "/error")
                                                .permitAll()

                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                                                .anyRequest().authenticated())

                                .formLogin(login -> login
                                                .loginPage("/usuario/login")
                                                .loginProcessingUrl("/usuario/login")
                                                .defaultSuccessUrl("/?loginSuccess=true", true)
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/usuario/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}