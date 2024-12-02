package com.salus.auth.jwt

import com.salus.user.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.context.annotation.Lazy


@Component
class JwtRequestFilter(
    private val jwtTokenUtil: JwtTokenUtil,  // Utilitário JWT usando Nimbus
    @Lazy private val userDetailsService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader("Authorization")

        // Verifica se o cabeçalho contém o token JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            // Extrai o username a partir do token
            val username = jwtTokenUtil.extractUsername(token)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                // Carrega os detalhes do usuário
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)

                // Valida o token
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    // Cria o objeto de autenticação e define no contexto de segurança
                    val authToken = jwtTokenUtil.getAuthentication(token, userDetails)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    // Atualiza o contexto de segurança com o token de autenticação
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }

        // Continua o fluxo da requisição
        chain.doFilter(request, response)
    }
}
