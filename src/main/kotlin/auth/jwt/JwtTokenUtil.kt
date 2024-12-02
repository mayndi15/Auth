package com.salus.auth.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenUtil {

    private val secret = "mySecretKey12345678901234567890123456789012" // Deve ter 256 bits (32 caracteres)

    fun generateToken(userDetails: UserDetails): String {
        val claimsSet = JWTClaimsSet.Builder()
            .subject(userDetails.username)
            .issueTime(Date())
            .expirationTime(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de expiração
            .build()

        val signer: JWSSigner = MACSigner(secret)

        val signedJWT = SignedJWT(
            com.nimbusds.jose.JWSHeader(JWSAlgorithm.HS256),
            claimsSet
        )

        signedJWT.sign(signer)
        return signedJWT.serialize() // Retorna o token JWT como string
    }

    // Extrai o username do token JWT
    fun extractUsername(token: String): String? {
        val signedJWT = SignedJWT.parse(token)
        return signedJWT.jwtClaimsSet.subject
    }

    // Valida o token JWT
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        try {
            val signedJWT = SignedJWT.parse(token)
            val verifier = MACVerifier(secret)

            // Verifica a assinatura e a expiração
            if (signedJWT.verify(verifier)) {
                val expiration = signedJWT.jwtClaimsSet.expirationTime
                return expiration.after(Date()) && signedJWT.jwtClaimsSet.subject == userDetails.username
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    // Cria um objeto de autenticação a partir do JWT
    fun getAuthentication(token: String, userDetails: UserDetails): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
    }
}