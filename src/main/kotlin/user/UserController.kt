package com.salus.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping()
    fun create(@RequestBody user: UserRequest): UserResponse {
        return userService.create(user)
    }

    @GetMapping("/{username}")
    fun findByUsername(@PathVariable username: String): UserResponse {
        return userService.details(username)
    }
}