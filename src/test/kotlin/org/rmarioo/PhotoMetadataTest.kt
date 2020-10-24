package org.rmarioo

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.time.Month.JULY

class HelloTest {

    @Test
    fun extractPhotoMetadata() {

        val file = File(HelloTest::class.java.getResource("/IMG_20200710_163551.jpg").file)
        val metadata: PhotoMetadata = extractPhotoMetadata(file)

        assertEquals(metadata.name, "IMG_20200710_163551.jpg")
        assertEquals(metadata.latitude, Latitude("45° 34' 54.41\""))
        assertEquals(metadata.longitude, Longitude("9° 17' 14.2\""))

        with(metadata.date) {
            assertEquals(year, 2020)
            assertEquals(month, JULY)
            assertEquals(dayOfMonth, 10)
            assertEquals(hour, 16)
            assertEquals(minute, 35)
            assertEquals(second, 53)
        }
    }



}
