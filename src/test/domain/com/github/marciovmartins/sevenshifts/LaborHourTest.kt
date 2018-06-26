package com.github.marciovmartins.sevenshifts

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.time.LocalDateTime
import java.time.Month
import java.util.concurrent.TimeUnit

object LaborHourTest : Spek({
    describe("Location settings have overtime starting at 8 hours daily, or 40 hours weekly") {
        on("Ryan works 5 days, 8 hours each (40 hours a week)") {
            val laborHours = LaborHour()

            (6..10).forEach {
                val day = it
                laborHours.add(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0)
                )
            }

            it("should not pay overtime") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), laborHours.normal)
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), laborHours.overtime)
            }
        }

        on("Evan works 40 hours for the whole week, but on Monday he works 12 hours and on Tuesday only 4 (and 8 hours Wednesday - Friday)") {
            val laborHours = LaborHour()

            laborHours.add(
                    LocalDateTime.of(2018, Month.JUNE, 6, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 6, 12, 0)
            )
            laborHours.add(
                    LocalDateTime.of(2018, Month.JUNE, 7, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 7, 4, 0)
            )
            (8..10).forEach {
                val day = it
                laborHours.add(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0)
                )
            }

            it("should pay 36 hours of normal pay and 4 hours of overtime pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(36), laborHours.normal)
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(4), laborHours.overtime)
            }
        }
    }
})