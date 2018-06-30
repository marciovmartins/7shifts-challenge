package com.github.marciovmartins.sevenshifts

interface UserRepository {
    fun findBy(userId: Long): User
}