package com.example.dotametrics.util

import org.junit.Assert.*
import org.junit.Test

class DatetimeTest {

    @Test
    fun formattingHumanReadableDateIsCorrect() {
        val expectedResult = "May 5, 2020"
        val testTime = 1588649430000 / 1000L

        val actualResult = Datetime.formatDate(testTime)

        assertEquals(expectedResult, actualResult)
    }
}