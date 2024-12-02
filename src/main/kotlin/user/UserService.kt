package com.salus.user

import com.salus.role.Role
import com.salus.role.RoleRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.context.annotation.Lazy

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    @Lazy private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserConfig {
        return userRepository.findByUsername(email)?.let { UserConfig(it) }
            ?: throw UsernameNotFoundException("User not found")
    }

    fun create(userRequest: UserRequest): UserResponse {
        val password = encode(userRequest.password)
        val roles = findByRoles(userRequest.roles)

        val user = User(
            username = userRequest.username,
            password = password,
            roles = roles
        )

        val dbUser = userRepository.save(user)

        val userResponse = UserResponse(
            id = dbUser.id,
            username = dbUser.username!!,
            roles = dbUser.roles.map { it.name!! }.toSet()
        )

        return userResponse
    }

    fun details(username: String): UserResponse {
        val user = userRepository.findByUsername(username)

        if (user != null) {
            return UserResponse(
                id = user.id,
                username = user.username!!,
                roles = user.roles.map { it.name!! }.toSet()
            )
        } else {
            throw RuntimeException("User not found")
        }
    }

    private fun encode(password: String): String {
        val encodedPassword = passwordEncoder.encode(password)
            ?: throw RuntimeException("Erro ao codificar a senha")
        return encodedPassword
    }

    private fun findByRoles(roles: Set<String>): Set<Role> {
        val verifiedRoles = roles.map { roleName ->
            roleRepository.findByName(roleName)
                ?: throw RuntimeException("Role não encontrada: $roleName")
        }.toSet()
        return verifiedRoles
    }
}