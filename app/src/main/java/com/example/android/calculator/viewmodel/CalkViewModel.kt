package com.example.android.calculator.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.calculator.R
import com.example.android.calculator.repository.RepositoryImpl
import kotlin.math.pow

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
    private var currentLine = zero
    private var recentLine = ""
    private var sign: String? = null

    private val repository = RepositoryImpl()
    val display = MutableLiveData(zero)
    val data = repository.getAll()

    fun setNumber(number: Int) {
        currentLine = if (pointOn) {
            if (currentLine.endsWith(".0")) "${
                currentLine.toDouble().toInt()
            }.$number" else "${currentLine.toDouble()}$number"
        } else {
            if (currentLine.toDouble() == 0.0) "${number.toDouble()}" else "${
                currentLine.toDouble().toInt()
            }$number.0"
        }
        display.value = currentLine
    }

    fun clear() {
        currentLine = zero
        recentLine = ""
        a = 0.0
        b = 0.0
        signOn = false
        pointOn = false
        resultOn = false
        display.value = currentLine
//        recentDisplay(recentLine)
    }

    fun backspace() {
        if (currentLine == zero) {
            pointOn = false
            signOn = false
            return
        } else if (currentLine.isBlank()) {
            currentLine = zero
            pointOn = false
            signOn = false
        } else if (currentLine.endsWith(".0")) {
            if (currentLine.length > 3) {
                currentLine = "" + currentLine.toDouble().toInt()
                currentLine = currentLine.substring(0, currentLine.length - 1) + ".0"
            } else {
                currentLine = zero
            }
            pointOn = false
            signOn = false
        } else currentLine = currentLine.substring(0, currentLine.length - 1)
        if (currentLine.endsWith(".")) {
            pointOn = false
            signOn = false
            currentLine += 0
        }
        display.value = currentLine
    }

    fun setSign(action: Actions) {
        if (signOn) calculateResult()
        a = currentLine.toDouble()
        sign = when (action) {
            Actions.PLUS -> plus
            Actions.MINUS -> minus
            Actions.MULTIPLY -> multiply
            Actions.DIVIDE -> divide
        }
        recentLine = currentLine + sign
        currentLine = zero
        currentAction = action
        signOn = true
        pointOn = false
//        recentDisplay(recentLine)
        display.value = currentLine
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
        if (currentLine.toDouble() != 0.0) currentLine = "" + currentLine.toDouble() * -1
        display.value = currentLine
    }

    fun factorial() {
        if (currentLine.endsWith(".0") && currentLine != zero) {
            var result = 1
            for (i in 1..currentLine.toDouble().toInt()) result *= i
            currentLine = "$result.0"
            display.value = currentLine
        }
    }

    fun pow() {
        currentLine = "${currentLine.toDouble().pow(2.0)}"
        display.value = currentLine
    }

    fun sqrt() {
        currentLine = "${kotlin.math.sqrt(currentLine.toDouble())}"
        display.value = currentLine
    }

    fun point() {
        pointOn = true
    }

    fun result() {
        if (currentAction == null) //отмена на случай, если не выбрано действие
            return
        calculateResult()
        resultOn = true
        pointOn = false
        signOn = false
//        recentDisplay(recentLine)
        display.value = currentLine
    }

    fun calculateResult() { //вычисление результата
        if (resultOn && !signOn) { //отображение истории при повторном нажатии на "="
            a = currentLine.toDouble()
            recentLine = "$currentLine$sign$b ="
        } else { //отображение истории при нажатии на "="
            b = currentLine.toDouble()
            recentLine = "$recentLine$currentLine ="
        }
        result = when (currentAction) {
            Actions.PLUS -> a + b
            Actions.MINUS -> a - b
            Actions.MULTIPLY -> a * b
            Actions.DIVIDE -> a / b
            else -> return
        }
        currentLine = "$result"
    }

    fun percent() { //вычисление процентов
        if (currentAction == null || resultOn) return
        b = currentLine.toDouble()
        recentLine = "$recentLine$currentLine % ="
        result = when (currentAction) {
            Actions.PLUS -> a + b * a / 100
            Actions.MINUS -> a - b * a / 100
            Actions.MULTIPLY -> a * b / 100
            Actions.DIVIDE -> a / b * 100
            else -> return
        }
        currentLine = "" + result
        resultOn = true
        pointOn = false
//        recentDisplay(recentLine)
        display.value = currentLine
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