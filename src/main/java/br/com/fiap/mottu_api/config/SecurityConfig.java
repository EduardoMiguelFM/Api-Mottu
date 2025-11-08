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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
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

        private static final RequestMatcher[] PUBLIC_API_MATCHERS = new RequestMatcher[] {
                        new AntPathRequestMatcher("/v3/api-docs/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/api/auth/**")
        };

        @Bean
        @Order(1)
        public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/**")
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers(PUBLIC_API_MATCHERS).permitAll()
                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .authenticationProvider(authenticationProvider());

                return http.build();
        }

        @Bean
        @Order(2)
        public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authz -> authz
                                                // Páginas públicas
                                                .requestMatchers("/", "/login", "/cadastro", "/css/**", "/js/**",
                                                                "/images/**")
                                                .permitAll()

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
                                                .csrfTokenRepository(
                                                                org.springframework.security.web.csrf.CookieCsrfTokenRepository
                                                                                .withHttpOnlyFalse()))
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .authenticationProvider(authenticationProvider());

                return http.build();
        }
}
