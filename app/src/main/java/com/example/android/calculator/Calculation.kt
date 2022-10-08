package com.example.android.calculator

data class Calculation(val id: Int, val expression: String, val history: MutableList<String>)
