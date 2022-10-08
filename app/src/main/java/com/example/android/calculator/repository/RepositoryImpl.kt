package com.example.android.calculator.repository

import androidx.lifecycle.MutableLiveData
import com.example.android.calculator.Calculation

class RepositoryImpl : Repository {
    private val calculations = emptyList<Calculation>()
    private val data = MutableLiveData(calculations)

    override fun getAll() = data

    override fun getById(id: Int) = data.value?.find { it.id == id }

    override fun set(calculation: Calculation) {
        data.value = data.value?.plus(calculation)
    }

    override fun clear() {

    }
}