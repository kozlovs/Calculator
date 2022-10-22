package com.example.android.calculator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.calculator.repository.RepositoryImpl
import kotlin.math.*

class CalkViewModel : ViewModel() {

    private var a = 0.0
    private var b = 0.0
    private var result = 0.0
    private var currentAction: Actions? = null
    private var pointOn = false
    private var resultOn = false
    private var signOn = false

    private var sign: String? = null

    private val repository = RepositoryImpl()
    val display = MutableLiveData(ZERO)
    val history = MutableLiveData(EMPTY)
    val data = repository.getAll()

    fun setNumber(number: Int) {
        display.value =
            if (pointOn) {
                if (isDisplayValueInteger())
                    "${getIntDisplayValue()}.$number"
                else
                    "${getDoubleDisplayValue()}$number"
            } else {
                if (isDisplayValueNull())
                    "${number.toDouble()}"
                else
                    "${getIntDisplayValue()}$number.0"
            }
    }

    private fun isDisplayValueInteger(): Boolean {
        return getStringDisplayValue().endsWith(".0")
    }

    private fun isDisplayValueNull(): Boolean {
        return getStringDisplayValue() == ZERO
    }

    private fun getStringDisplayValue() = display.value.toString()

    private fun getDoubleDisplayValue() = getStringDisplayValue().toDouble()

    private fun getIntDisplayValue() = getDoubleDisplayValue().toInt()

    private fun getDisplayLength() = getStringDisplayValue().length

    fun clear() {
        display.value = ZERO
        history.value = EMPTY
        a = 0.0
        b = 0.0

        pointOn = false
        signOn = false
        resultOn = false
    }

    fun backspace() {
        if (display.value == ZERO) {
            pointOn = false
            signOn = false
            return
        } else if (getStringDisplayValue().isBlank()) {
            display.value = ZERO
            pointOn = false
            signOn = false
        } else if (isDisplayValueInteger()) {
            if (getDisplayLength() > 3) {
                display.value = getIntDisplayValue().toString()
                display.value = getStringDisplayValue().substring(0, getDisplayLength() - 1) + ".0"
            } else {
                display.value = ZERO
            }
            pointOn = false
            signOn = false
        } else
            display.value = getStringDisplayValue().substring(0, getDisplayLength() - 1)
        if (getStringDisplayValue().endsWith(".")) {
            pointOn = false
            signOn = false
            display.value += 0
        }
    }

    private fun setSign(action: Actions) {
        if (signOn) calculateResult()
        a = getDoubleDisplayValue()
        sign = when (action) {
            Actions.PLUS -> PLUS
            Actions.MINUS -> MINUS
            Actions.MULTIPLY -> MULTIPLY
            Actions.DIVIDE -> DIVIDE
        }
        history.value = getStringDisplayValue() + sign
        display.value = ZERO
        currentAction = action
        signOn = true
        pointOn = false
    }

    fun plus() {
        setSign(Actions.PLUS)
    }

    fun minus() {
        setSign(Actions.MINUS)
    }

    fun multiply() {
        setSign(Actions.MULTIPLY)
    }

    fun divide() {
        setSign(Actions.DIVIDE)
    }

    fun changeSign() {
        if (getStringDisplayValue() != ZERO)
            display.value = (getDoubleDisplayValue() * -1).toString()
    }

    fun factorial() {
        if (isDisplayValueInteger() && getStringDisplayValue() != ZERO) {
            var result = 1.0
            for (i in 1..getIntDisplayValue()) result *= i
            display.value = "$result"
        }
    }

    fun pow() {
        display.value = getDoubleDisplayValue().pow(2.0).toString()
    }

    fun sqrt() {
        display.value = sqrt(getDoubleDisplayValue()).toString()
    }

    fun point() {
        pointOn = true
    }

    fun result() {
        if (currentAction != null) {
            calculateResult()
            resultOn = true
            pointOn = false
            signOn = false
        }
    }

    private fun calculateResult() { //вычисление результата
        if (resultOn && !signOn) { //отображение истории при повторном нажатии на "="
            a = getDoubleDisplayValue()
            history.value = "${display.value}$sign$b ="
        } else { //отображение истории при нажатии на "="
            b = getDoubleDisplayValue()
            history.value = "${history.value} ${getStringDisplayValue()} ="
        }
        result = when (currentAction) {
            Actions.PLUS -> a + b
            Actions.MINUS -> a - b
            Actions.MULTIPLY -> a * b
            Actions.DIVIDE -> a / b
            else -> return
        }
        display.value = result.toString()
    }

    fun percent() { //вычисление процентов
        if (currentAction == null || resultOn) return
        b = getDoubleDisplayValue()
        history.value = "${history.value}${getStringDisplayValue()} % ="
        result = when (currentAction) {
            Actions.PLUS -> a + b * a / 100
            Actions.MINUS -> a - b * a / 100
            Actions.MULTIPLY -> a * b / 100
            Actions.DIVIDE -> a / b * 100
            else -> return
        }
        display.value = result.toString()
        resultOn = true
        pointOn = false
    }

    fun copy() {
//        val text = getStringDisplayValue()
//        val clipData = ClipData.newPlainText("text", text)
//        val clipboardManager =
//            getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
//        clipboardManager.setPrimaryClip(clipData)
    }

    companion object {
        private const val PLUS = " + "
        private const val MINUS = " - "
        private const val MULTIPLY = " \u00D7 "
        private const val DIVIDE = " \u00F7 "
        private const val ZERO = "0.0"
        private const val EMPTY = ""
    }
}