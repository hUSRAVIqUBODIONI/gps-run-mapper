package com.example.runapp.data.location

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
{
    override fun equals(other: Any?): Boolean {
        return other is LocationData &&
                latitude == other.latitude &&
                longitude == other.longitude
    }

    override fun hashCode(): Int {
        return 31 * latitude.hashCode() + longitude.hashCode()
    }
}
