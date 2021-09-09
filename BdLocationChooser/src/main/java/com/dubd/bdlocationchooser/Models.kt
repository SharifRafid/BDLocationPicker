package com.dubd.bdlocationchooser

import org.json.JSONObject

class District(json : String) : JSONObject(json){
    val id = this.optString("id").toString().toInt()
    val name = this.optString("name").toString()
    val bengaliName = this.optString("bn_name").toString()
    val lat = this.optString("lat").toString()
    val long = this.optString("long").toString()
    val divisionId = this.optString("division_id").toString().toInt()
}
class Upazila(json : String) : JSONObject(json){
    val id = this.optString("id").toString().toInt()
    val name = this.optString("name").toString()
    val bengaliName = this.optString("bn_name").toString()
    val districtId = this.optString("district_id").toString().toInt()
}
class Division(json : String) : JSONObject(json){
    val id = this.optString("id").toString().toInt()
    val name = this.optString("name").toString()
    val bengaliName = this.optString("bn_name").toString()
    val lat = this.optString("lat").toString()
    val long = this.optString("long").toString()
}