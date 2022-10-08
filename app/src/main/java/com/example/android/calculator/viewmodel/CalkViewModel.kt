package com.example.android.calculator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.calculator.repository.RepositoryImpl
import kotlin.math.*

class CalkViewModel : ViewModel() {
    private val plus = " + "
    private val minus = " - "
    private val multiply = " \u00D7 "
    private val divide = " \u00F7 "
    private val zero = "0.0"
    private var a = 0.0
    private var b = 0.0
    private var result = 0.0
    private var currentAction: Actions? = null
    private var pointOn = false
    private var resultOn = false
    private var signOn = false
    private var empty = ""
    private var sign: String? = null

    private val repository = RepositoryImpl()
    val display = MutableLiveData(zero)
    val history = MutableLiveData("")
    val data = repository.getAll()

    fun setNumber(number: Int) {

        display.value = if (pointOn) {
            if (display.value.toString().endsWith(".0")) "${
                display.value.toString().toDouble().toInt()
            }.$number" else "${display.value.toString().toDouble()}$number"
        } else {
            if (display.value.toString().toDouble() == 0.0) "${number.toDouble()}" else "${
                display.value.toString().toDouble().toInt()
            }$number.0"
        }
    }

    fun clear() {
        display.value = zero
        history.value = empty
        a = 0.0
        b = 0.0
        signOn = false
        pointOn = false
        resultOn = false
    }

    fun backspace() {
        if (display.value == zero) {
            pointOn = false
            signOn = false
            return
        } else if (display.value.toString().isBlank()) {
            display.value = zero
            pointOn = false
            signOn = false
        } else if (display.value.toString().endsWith(".0")) {
            if (display.value.toString().length > 3) {
                display.value = "" + display.value.toString().toDouble().toInt()
                display.value = display.value.toString()
                    .substring(0, display.value.toString().length - 1) + ".0"
            } else {
                display.value = zero
            }
            pointOn = false
            signOn = false
        } else display.value =
            display.value.toString().substring(0, display.value.toString().length - 1)
        if (display.value.toString().endsWith(".")) {
            pointOn = false
            signOn = false
            display.value += 0
        }
    }

    private fun setSign(action: Actions) {
        if (signOn) calculateResult()
        a = display.value.toString().toDouble()
        sign = when (action) {
            Actions.PLUS -> plus
            Actions.MINUS -> minus
            Actions.MULTIPLY -> multiply
            Actions.DIVIDE -> divide
        }
        history.value = display.value.toString() + sign
        display.value = zero
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
        if (display.value.toString().toDouble() != 0.0)
            display.value = (display.value.toString().toDouble() * -1).toString()
    }

    fun factorial() {
        if (display.value.toString().endsWith(".0") && display.value.toString() != zero) {
            var result = 1
            for (i in 1..display.value.toString().toDouble().toInt()) result *= i
            display.value = "$result.0"
        }
    }

    fun pow() {
        display.value = display.value.toString().toDouble().pow(2.0).toString()
    }

    fun sqrt() {
        display.value = sqrt(display.value.toString().toDouble()).toString()
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
            a = display.value.toString().toDouble()
            history.value = "${display.value}$sign$b ="
        } else { //отображение истории при нажатии на "="
            b = display.value.toString().toDouble()
            history.value = "${history.value.toString()} ${display.value.toString()} ="
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
        b = display.value.toString().toDouble()
        history.value = "${history.value.toString()}${display.value.toString()} % ="
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
//        val text = binding.currentDisplayTextView.text.toString()
//        val clipData = ClipData.newPlainText("text", text)
//        val clipboardManager =
//            getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
//        clipboardManager.setPrimaryClip(clipData)
//        Toast.makeText(applicationContext, getString(R.string.copied), Toast.LENGTH_SHORT)
//            .show()
    }
}