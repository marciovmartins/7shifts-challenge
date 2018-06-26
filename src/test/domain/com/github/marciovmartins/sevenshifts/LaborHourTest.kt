package com.github.marciovmartins.sevenshifts

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import java.time.LocalDateTime
import java.time.Month

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
                Assert.assertEquals(2400, laborHours.normal)
                Assert.assertEquals(0, laborHours.overtime)
            }
        }
    }
})