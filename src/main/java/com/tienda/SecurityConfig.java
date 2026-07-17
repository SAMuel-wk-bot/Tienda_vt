/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda;

/**
 *
 * @author psala
 */


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.authorizeHttpRequests(authorize -> authorize

                /*
                 * Páginas públicas.
                 */
                .requestMatchers(
                        "/",
                        "/index",
                        "/login",
                        "/accesoDenegado",
                        "/error",
                        "/favicon.ico"
                ).permitAll()

                /*
                 * Recursos públicos.
                 * Esto permite que Bootstrap, Font Awesome,
                 * JavaScript y los estilos CSS carguen correctamente.
                 */
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                ).permitAll()

                /*
                 * El vendedor y el administrador pueden ver
                 * los listados y ejecutar consultas.
                 */
                .requestMatchers(
                        "/categoria/listado",
                        "/producto/listado",
                        "/consultas/**"
                ).hasAnyRole("VENDEDOR", "ADMIN")

                /*
                 * Solamente el administrador puede guardar,
                 * modificar y eliminar categorías o productos.
                 */
                .requestMatchers(
                        "/categoria/**",
                        "/producto/**",
                        "/admin/**",
                        "/usuario/**"
                ).hasRole("ADMIN")

                /*
                 * Rutas preparadas para clientes.
                 */
                .requestMatchers(
                        "/carrito/**",
                        "/facturar/**"
                ).hasAnyRole("USUARIO", "ADMIN")

                /*
                 * Cualquier otra ruta requiere iniciar sesión.
                 */
                .anyRequest()
                .authenticated()
        );

        /*
         * Configuración del inicio de sesión.
         */
        http.formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        /*
         * Configuración del cierre de sesión.
         */
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        );

        /*
         * Página mostrada cuando el usuario no tiene permisos.
         */
        http.exceptionHandling(exception -> exception
                .accessDeniedPage("/accesoDenegado")
        );

        return http.build();
    }

    /*
     * Codificador de contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Usuarios temporales almacenados en memoria.
     */
    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails juan = User.builder()
                .username("Juan")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails rebeca = User.builder()
                .username("Rebeca")
                .password(passwordEncoder.encode("456"))
                .roles("VENDEDOR")
                .build();

        UserDetails pedro = User.builder()
                .username("Pedro")
                .password(passwordEncoder.encode("789"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(
                juan,
                rebeca,
                pedro
        );
    }
}