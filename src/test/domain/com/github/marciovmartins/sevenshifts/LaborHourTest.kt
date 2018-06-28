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

            (4..8).forEach {
                val day = it
                laborHours.add(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0)
                )
            }

            it("should pay 40 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), laborHours.normal)
            }
            it("should not pay overtime") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), laborHours.overtime)
            }
        }

        on("Evan works 40 hours for the whole week, but on Monday he works 12 hours and on Tuesday only 4 (and 8 hours Wednesday - Friday)") {
            val laborHours = LaborHour()

            laborHours.add(
                    LocalDateTime.of(2018, Month.JUNE, 4, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 4, 12, 0)
            )
            laborHours.add(
                    LocalDateTime.of(2018, Month.JUNE, 5, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 5, 4, 0)
            )
            (6..8).forEach {
                val day = it
                laborHours.add(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0)
                )
            }

            it("should pay 36 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(36), laborHours.normal)
            }
            it("should pay 4 hours of overtime pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(4), laborHours.overtime)
            }
        }

        on("Martina works 48 hours in a week (8 hours Monday - Saturday)") {
            val laborHours = LaborHour()

            (4..9).forEach {
                val day = it
                laborHours.add(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0)
                )
            }

            it("should paid 40 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), laborHours.normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), laborHours.overtime)
            }
        }

        on("Jeff works over night 8 hours normal rate and 34 minutes overtime pay rate") {
            val laborHours = LaborHour()

            laborHours.add(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 21, 11),
                    LocalDateTime.of(2017, Month.OCTOBER, 12, 5, 45)
            )

            it("should paid 8 hours normal rate ") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), laborHours.normal)
            }
            it("should paid 34 minutes overtime pay rate") {
                Assert.assertEquals(34, laborHours.overtime)
            }
        }

        on("Jeff works 8 hours normal rate and 8 hours overtime pay rate") {
            val laborHours = LaborHour()

            laborHours.add(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 0, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 8, 0)
            )
            laborHours.add(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 12, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 20, 0)
            )

            it("should paid 8 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), laborHours.normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), laborHours.overtime)
            }
        }
    }
})