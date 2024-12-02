package com.salus.config

import com.salus.auth.jwt.JwtRequestFilter
import com.salus.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.context.annotation.Lazy

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Lazy private val jwtRequestFilter: JwtRequestFilter,
    private val userDetailsService: UserService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/users/**").permitAll()// Rota pública para autenticação
                    //.requestMatchers("/api/admin/**").hasRole("ADMIN")  // Proteção baseada em papéis
                    //.requestMatchers("/api/person/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()  // Qualquer outra requisição deve ser autenticada
            }
            .addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )  // Adiciona o filtro JWT
            .csrf { csrf -> csrf.disable() }  // Desabilita CSRF (se aplicável para APIs REST com JWT)

        return http.build()
    }

    // Configura o AuthenticationManager
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}