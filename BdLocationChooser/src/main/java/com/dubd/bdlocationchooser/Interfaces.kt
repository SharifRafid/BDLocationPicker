package com.dubd.bdlocationchooser

interface LocationChooseListener{
    fun onLocationChoose(division: Division, district: District, upazila: Upazila)
    fun onCancelled()
}