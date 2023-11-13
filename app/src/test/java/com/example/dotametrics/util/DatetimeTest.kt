package com.example.dotametrics.util

import org.junit.Assert.*
import org.junit.Test

class DatetimeTest {

    @Test
    fun convertingTimestampToDateIsCorrect() {
        val expectedResult = "55834-08-10T11:10:41"
        val testTime = 1699802788241

        val method = Datetime.javaClass.getDeclaredMethod("getDateTime", Long::class.java)
        method.isAccessible = true

        val actualResult = method.invoke(Datetime, testTime)
        assertEquals(expectedResult, actualResult)
    }
}