package com.github.marciovmartins.sevenshifts

class User(location: Location) {
    val labourHours = LabourHours(location.labourSettings)
}