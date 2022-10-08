package com.example.android.calculator.repository

import androidx.lifecycle.LiveData
import com.example.android.calculator.Calculation

interface Repository {
    fun getAll(): LiveData<List<Calculation>>
    fun getById(id: Int): Calculation?
    fun set(calculation: Calculation)
    fun clear()
}