package com.github.marciovmartins.sevenshifts

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.time.LocalDateTime
import java.time.Month
import java.util.concurrent.TimeUnit

object LabourHourTest : Spek({
    describe("Location settings have overtime starting at 8 hours daily, or 40 hours weekly") {
        val userId = 1234L
        val labourSettings = LabourSettings(
                TimeUnit.HOURS.toMinutes(8),
                TimeUnit.HOURS.toMinutes(40)
        )

        on("Ryan works 5 days, 8 hours each (40 hours a week)") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2018, Month.JUNE, 4, 9, 0)

            (4..8).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0),
                        userId
                ))
            }

            it("should pay 40 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.getFrom(week).normal)
            }
            it("should not pay overtime") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), labourHours.getFrom(week).overtime)
            }
        }

        on("Evan works 40 hours for the whole week, but on Monday he works 12 hours and on Tuesday only 4 (and 8 hours Wednesday - Friday)") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2018, Month.JUNE, 4, 9, 0)

            labourHours.add(Punch(
                    LocalDateTime.of(2018, Month.JUNE, 4, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 4, 12, 0),
                    userId
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2018, Month.JUNE, 5, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 5, 4, 0),
                    userId
            ))
            (6..8).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0),
                        userId
                ))
            }

            it("should pay 36 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(36), labourHours.getFrom(week).normal)
            }
            it("should pay 4 hours of overtime pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(4), labourHours.getFrom(week).overtime)
            }
        }

        on("Martina works 48 hours in a week (8 hours Monday - Saturday)") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2018, Month.JUNE, 4, 9, 0)

            (4..9).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0),
                        userId
                ))
            }

            it("should paid 40 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.getFrom(week).normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).overtime)
            }
        }

        on("Jeff works over night 8 hours normal rate and 34 minutes overtime pay rate") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2017, Month.OCTOBER, 11, 9, 0)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 21, 11),
                    LocalDateTime.of(2017, Month.OCTOBER, 12, 5, 45),
                    userId
            ))

            it("should paid 8 hours normal rate ") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).normal)
            }
            it("should paid 34 minutes overtime pay rate") {
                Assert.assertEquals(34, labourHours.getFrom(week).overtime)
            }
        }

        on("Jeff works 8 hours normal rate and 8 hours overtime pay rate") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2017, Month.OCTOBER, 11, 9, 0)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 0, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 8, 0),
                    userId
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 12, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 20, 0),
                    userId
            ))

            it("should paid 8 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).overtime)
            }
        }

        on("Jeff works 12 hours (8 hours normal rate + 4 hours overtime pay rate) and another 4 hours overtime") {
            val labourHours = LabourHours(labourSettings)
            val week = LocalDateTime.of(2017, Month.OCTOBER, 11, 9, 0)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 0, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 12, 0),
                    userId
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 16, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 20, 0),
                    userId
            ))

            it("should paid 8 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.getFrom(week).overtime)
            }
        }

        on("Ryan works 5 days, 8 hours each (40 hours a week) in 2 weeks") {
            val labourHours = LabourHours(labourSettings)
            val firstWeek = LocalDateTime.of(2018, Month.JUNE, 4, 9, 0)
            val secondWeek = LocalDateTime.of(2018, Month.JUNE, 11, 9, 0)

            (4..8).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0),
                        userId
                ))
            }
            (11..15).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0),
                        userId
                ))
            }

            it("should pay 40 hours of normal pay in the first week") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.getFrom(firstWeek).normal)
            }
            it("should not pay overtime in the first week") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), labourHours.getFrom(firstWeek).overtime)
            }
            it("should pay 40 hours of normal pay in the second week") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.getFrom(secondWeek).normal)
            }
            it("should not pay overtime in the second week") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), labourHours.getFrom(secondWeek).overtime)
            }
        }
    }
})