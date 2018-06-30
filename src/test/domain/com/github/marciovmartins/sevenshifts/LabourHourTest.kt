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
        val labourSettings = LabourSettings(
                TimeUnit.HOURS.toMinutes(8),
                TimeUnit.HOURS.toMinutes(40)
        )
        val location = Location(labourSettings)

        on("Ryan works 5 days, 8 hours each (40 hours a week)") {
            val labourHours = LabourHour(location)

            (4..8).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 9, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 17, 0)
                ))
            }

            it("should pay 40 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.normal)
            }
            it("should not pay overtime") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(0), labourHours.overtime)
            }
        }

        on("Evan works 40 hours for the whole week, but on Monday he works 12 hours and on Tuesday only 4 (and 8 hours Wednesday - Friday)") {
            val labourHours = LabourHour(location)

            labourHours.add(Punch(
                    LocalDateTime.of(2018, Month.JUNE, 4, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 4, 12, 0)
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2018, Month.JUNE, 5, 0, 0),
                    LocalDateTime.of(2018, Month.JUNE, 5, 4, 0)
            ))
            (6..8).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0)
                ))
            }

            it("should pay 36 hours of normal pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(36), labourHours.normal)
            }
            it("should pay 4 hours of overtime pay") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(4), labourHours.overtime)
            }
        }

        on("Martina works 48 hours in a week (8 hours Monday - Saturday)") {
            val labourHours = LabourHour(location)

            (4..9).forEach {
                val day = it
                labourHours.add(Punch(
                        LocalDateTime.of(2018, Month.JUNE, day, 0, 0),
                        LocalDateTime.of(2018, Month.JUNE, day, 8, 0)
                ))
            }

            it("should paid 40 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(40), labourHours.normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.overtime)
            }
        }

        on("Jeff works over night 8 hours normal rate and 34 minutes overtime pay rate") {
            val labourHours = LabourHour(location)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 21, 11),
                    LocalDateTime.of(2017, Month.OCTOBER, 12, 5, 45)
            ))

            it("should paid 8 hours normal rate ") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.normal)
            }
            it("should paid 34 minutes overtime pay rate") {
                Assert.assertEquals(34, labourHours.overtime)
            }
        }

        on("Jeff works 8 hours normal rate and 8 hours overtime pay rate") {
            val labourHours = LabourHour(location)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 0, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 8, 0)
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 12, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 20, 0)
            ))

            it("should paid 8 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.overtime)
            }
        }

        on("Jeff works 12 hours (8 hours normal rate + 4 hours overtime pay rate) and another 4 hours overtime") {
            val labourHours = LabourHour(location)

            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 0, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 12, 0)
            ))
            labourHours.add(Punch(
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 16, 0),
                    LocalDateTime.of(2017, Month.OCTOBER, 11, 20, 0)
            ))

            it("should paid 8 hours normal rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.normal)
            }
            it("should paid 8 hours overtime pay rate") {
                Assert.assertEquals(TimeUnit.HOURS.toMinutes(8), labourHours.overtime)
            }
        }
    }
})