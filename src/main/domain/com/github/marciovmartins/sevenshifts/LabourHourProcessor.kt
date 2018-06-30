package com.github.marciovmartins.sevenshifts

class LabourHourProcessor(
        private val userRepository: UserRepository
) {
    fun process(punches: List<Punch>): List<User> {
        return punches.map {
            val user = userRepository.findBy(it.userId)
            user.labourHours.add(it)
            user
        }
    }
}
