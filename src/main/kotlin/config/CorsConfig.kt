package com.salus.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        config.allowedOrigins = listOf("*")  // Permitir todas as origens
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
        config.allowedHeaders = listOf("*")  // Permitir todos os cabeçalhos
        config.allowCredentials = true  // Permitir credenciais (como cookies)

        source.registerCorsConfiguration("/**", config)  // Aplica a configuração CORS globalmente
        return source
    }

    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter(corsConfigurationSource())
    }
}