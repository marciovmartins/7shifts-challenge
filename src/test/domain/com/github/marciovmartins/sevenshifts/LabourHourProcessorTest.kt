package com.github.marciovmartins.sevenshifts

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.time.LocalDateTime
import java.time.Month

object LabourHourProcessorTest : Spek({
    val userId = 1234L

    describe("Process data with one user") {
        val punches = listOf(
                Punch(
                        LocalDateTime.of(2018, Month.JUNE, 4, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, 4, 17, 0),
                        userId
                )
        )

        val labourHoursMocked = mock<LabourHour>()
        val user = mock<User>()
        whenever(user.labourHours).thenReturn(labourHoursMocked)

        val userRepository = mock<UserRepository> {
            on { findBy(userId) } doReturn user
        }

        on("with one punch") {
            val labourHourProcessor = LabourHourProcessor(userRepository)
            val users = labourHourProcessor.process(punches)

            it("should have one user") {
                Assert.assertEquals(1, users.size)
            }
            it("should have the expected user") {
                Assert.assertEquals(user, users[0])
            }
            it("should have call labourHours.add(Punch) once") {
                verify(labourHoursMocked).add(punches[0])
            }
        }
    }
})