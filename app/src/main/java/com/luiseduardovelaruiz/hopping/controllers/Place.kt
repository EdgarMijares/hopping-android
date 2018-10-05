package com.luiseduardovelaruiz.hopping.controllers

class Place(
        val placename: String,
        val profileimage: String,
        val id_place: String,
        val latitude: String,
        val longitude: String,
        val description: String,
        val contactnumber: String,
        val address: String,
        val backgroundimage: String,
        val id_user: String,
        val promos: Map<String, String>)

