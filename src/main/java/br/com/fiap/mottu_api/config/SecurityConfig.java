package br.com.fiap.mottu_api.config;

import br.com.fiap.mottu_api.service.UsuarioPatioService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final UsuarioPatioService usuarioPatioService;
        private final PasswordEncoder passwordEncoder;
        private final CorsConfigurationSource corsConfigurationSource;

        public SecurityConfig(UsuarioPatioService usuarioPatioService, PasswordEncoder passwordEncoder,
                        @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource) {
                this.usuarioPatioService = usuarioPatioService;
                this.passwordEncoder = passwordEncoder;
                this.corsConfigurationSource = corsConfigurationSource;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(usuarioPatioService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authz -> authz
                                                // Páginas públicas
                                                .requestMatchers("/", "/login", "/cadastro", "/css/**", "/js/**",
                                                                "/images/**")
                                                .permitAll()

                                                // API pública para Swagger
                                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**",
                                                                "/swagger-ui.html")
                                                .permitAll()

                                                // API pública para autenticação mobile
                                                .requestMatchers("/api/auth/**").permitAll()
                                                
                                                // API pública para testes (remover em produção)
                                                .requestMatchers("/api/motos/**", "/api/patios/**").permitAll()

                                                // Rotas administrativas
                                                .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                                                // Rotas de supervisor
                                                .requestMatchers("/supervisor/**", "/api/supervisor/**")
                                                .hasAnyRole("ADMIN", "SUPERVISOR")

                                                // Dashboard e rotas de usuário comum
                                                .requestMatchers("/dashboard").hasAnyRole("ADMIN", "SUPERVISOR", "USER")
                                                .requestMatchers("/motos/**", "/patios/**")
                                                .hasAnyRole("ADMIN", "SUPERVISOR", "USER")
                                                .requestMatchers("/api/usuarios/**")
                                                .hasAnyRole("ADMIN", "SUPERVISOR", "USER")

                                                // Todas as outras requisições precisam de autenticação
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**")
                                                .csrfTokenRepository(
                                                                org.springframework.security.web.csrf.CookieCsrfTokenRepository
                                                                                .withHttpOnlyFalse()))
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .authenticationProvider(authenticationProvider());

                return http.build();
        }
}
