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
        metadata.coordinate(),
        metadata.originalDate()
    )
}

private fun Metadata.fileName(): String = valueFor("File", "File Name")


private fun Metadata.coordinate(): Coordinate = Coordinate(latitude(),longitude())

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
    val coordinate: Coordinate,
    val date: LocalDateTime
)

