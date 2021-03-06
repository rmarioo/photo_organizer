package org.rmarioo

import org.hamcrest.CoreMatchers.`is`
import org.json.JSONObject
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.time.Month.JULY

class PhotoMetadataTest {

    private val DELTA = 0.01

    @Test
    fun extractPhotoMetadata() {

        val file = File(PhotoMetadataTest::class.java.getResource("/IMG_20200710_163551.jpg").file)
        val metadata: PhotoMetadata = extractPhotoMetadata(file)

        assertEquals(metadata.name, "IMG_20200710_163551.jpg")
        assertEquals(metadata.coordinate.latitude, Latitude(45, 34, 54.41))
        assertEquals(metadata.coordinate.longitude, Longitude(9, 17, 14.2))

        with(metadata.date) {
            assertEquals(year, 2020)
            assertEquals(month, JULY)
            assertEquals(dayOfMonth, 10)
            assertEquals(hour, 16)
            assertEquals(minute, 35)
            assertEquals(second, 53)
        }
    }


    @Test
    fun `convert to decimal points`() {
        val latDecimal = Latitude(45, 34, 54.41).toDecimalPoint()
        assertEquals(latDecimal, 45.581781, DELTA)

        val longDecimal = Longitude(9, 17, 14.2).toDecimalPoint()
        assertEquals(longDecimal, 9.287278, DELTA)
    }

    @Test
    fun `khttp restClient test`() {
        val r = khttp.get("http://dummy.restapiexample.com/api/v1/employee/1")

        val data= r.jsonObject["data"] as JSONObject
        val name = data.getString("employee_name")

        Assert.assertThat(name, `is`("Tiger Nixon"))
    }
}
