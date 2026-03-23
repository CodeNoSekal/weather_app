package com.polyhub.weather.api


data class LocationUI (
    val name: String,
    val latitude: String,
    val longitude: String
)


fun List<LocationResponse>.toUiModel(): LocationUI {

    val location = this.first()

    if (location.localNames != null){
        if (location.localNames.ru != null){

            val targetIndex = location.localNames.ru
                .indexOfFirst { it.isUpperCase() }
                .let { if (it == -1) 0 else it }

            val name = location.localNames.ru.drop(targetIndex)

            return LocationUI(
                name,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }

    val targetIndex = location.name
        .indexOfFirst { it.isUpperCase() }
        .let { if (it == -1) 0 else it }

    val name = location.name.drop(targetIndex)

    return LocationUI(
        name,
        latitude = location.latitude,
        longitude = location.longitude
    )
}