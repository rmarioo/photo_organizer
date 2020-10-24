package org.rmarioo

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun extractPhotoMetadata(file: File): PhotoMetadata {
    val metadata = ImageMetadataReader.readMetadata(file)

    return PhotoMetadata(
        metadata.fileName(),
        metadata.latitude(),
        metadata.longitude(),
        metadata.originalDate()
    )
}

private fun Metadata.fileName(): String = valueFor("File", "File Name")

private fun Metadata.latitude(): Latitude {
    val valStr = valueFor("GPS", "GPS Latitude")

    return Latitude.fromString(valStr)
}

private fun Metadata.longitude(): Longitude {
    val value = valueFor("GPS", "GPS Longitude")
    return Longitude.fromString(value)
}

private fun Metadata.originalDate(): LocalDateTime =
    parseOriginalDate(valueFor("Exif SubIFD", "Date/Time Original"))

private fun parseOriginalDate(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
    return LocalDateTime.parse(dateString, formatter)
}

private fun Metadata.valueFor(directoryName: String, tagName: String): String {
    return directories.filter { directory -> directory.name == directoryName }[0].tags
                      .filter { tag -> tag.tagName == tagName }[0].description
}


data class PhotoMetadata(
    val name: String,
    val latitude: Latitude,
    val longitudeOld: Longitude,
    val date: LocalDateTime
)

data class Latitude(val degree: Int,val minutes:Int, val seconds: Double) {

    fun toDecimalPoint(): Double {
        return toDecimalPoint(degree,minutes,seconds)
    }
    companion object {
        fun fromString( valStr: String): Latitude {

            val (degree, minutes, seconds) = Coordinate.fromString(valStr)

            return Latitude(degree,minutes,seconds)
        }
    }
}

data class Longitude(val degree: Int,val minutes:Int, val seconds: Double) {

    fun toDecimalPoint(): Double {
        return toDecimalPoint(degree,minutes,seconds)
    }

    companion object {
        fun fromString( valStr: String): Longitude {

            val (degree, minutes, seconds) = Coordinate.fromString(valStr)

            return Longitude(degree,minutes,seconds)
        }
    }
}

data class Coordinate(val degree: Int,val minutes:Int, val seconds: Double) {

    companion object {
        fun fromString( valStr: String): Coordinate {
            val intAndMinutesPlusSeconds = valStr.split("°")
            val degree =   intAndMinutesPlusSeconds[0].toInt()

            val minutesAndSeconds = intAndMinutesPlusSeconds[1].split("'")
            val minutes = minutesAndSeconds[0].trim().toInt()

            val seconds = minutesAndSeconds[1].replace("\"", "").trim().toDouble()

            return Coordinate(degree,minutes,seconds)
        }


    }
}
 fun toDecimalPoint(degree: Int, minutes:Int, seconds: Double) = degree + (seconds.div(60.0) + minutes).div(60.0)


