package com.github.marciovmartins.sevenshifts

class User(location: Location) {
    val labourHours = LabourHour(location.labourSettings)
}