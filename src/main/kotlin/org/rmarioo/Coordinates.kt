package org.rmarioo

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


data class Coordinate(val latitude: Latitude,val longitude: Longitude)

fun Coordinate.distanceInKmTo(other: Coordinate): Double = calculateDistance(this, other,"K")

data class Latitude(val degree: Int,val minutes:Int, val seconds: Double) {

    fun toDecimalPoint(): Double {
        return toDecimalPoint(degree,minutes,seconds)
    }
    companion object {
        fun fromString( valStr: String): Latitude {

            val (degree, minutes, seconds) = coordinateValuesFromString(valStr)

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

            val (degree, minutes, seconds) = coordinateValuesFromString(valStr)

            return Longitude(degree,minutes,seconds)
        }
    }
}

fun coordinateValuesFromString(valStr: String): Triple<Int, Int, Double> {
    val intAndMinutesPlusSeconds = valStr.split("°")
    val degree =   intAndMinutesPlusSeconds[0].toInt()

    val minutesAndSeconds = intAndMinutesPlusSeconds[1].split("'")
    val minutes = minutesAndSeconds[0].trim().toInt()

    val seconds = minutesAndSeconds[1].replace("\"", "").trim().toDouble()

    return Triple(degree,minutes,seconds)
}

fun toDecimalPoint(degree: Int, minutes:Int, seconds: Double) = degree + (seconds.div(60.0) + minutes).div(60.0)


private fun calculateDistance(
    point1: Coordinate,
    point2: Coordinate,
    unit: String
): Double {
    val lat1 = point1.latitude.toDecimalPoint()
    val lon1 = point1.longitude.toDecimalPoint()
    val lat2 = point2.latitude.toDecimalPoint()
    val lon2 = point2.longitude.toDecimalPoint()

    return if (lat1 == lat2 && lon1 == lon2)
        0.0
    else {
        val theta = lon1 - lon2
        var dist = sin(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) + cos(
            Math.toRadians(lat1)
        ) * cos(Math.toRadians(lat2)) * cos(Math.toRadians(theta))
        dist = acos(dist)
        dist = Math.toDegrees(dist)
        dist *= 60 * 1.1515
        if (unit == "K") {
            dist *= 1.609344
        } else if (unit == "N") {
            dist *= 0.8684
        }
        round2Decimals(dist)
    }
}

private fun round2Decimals(dist1: Double) = Math.round(dist1 * 10.0) / 10.0


