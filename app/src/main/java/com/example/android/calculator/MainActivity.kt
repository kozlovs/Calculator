package com.example.android.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
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
    private var recentTextView: TextView? = null
    private var currentTextView: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        recentTextView = findViewById(R.id.recentDisplayTextView)
        currentTextView = findViewById(R.id.currentDisplayTextView)
        progressBar = findViewById(R.id.progress_bar)
        findViewById<ImageButton>(R.id.button_copy).setOnClickListener {
            val text = currentTextView?.text as String
            val clipData = ClipData.newPlainText("text", text)
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(applicationContext, getString(R.string.copied), Toast.LENGTH_SHORT)
                .show()
        }
        findViewById<Button>(R.id.button_0).setOnClickListener {
            setNumber(0)
        }
        findViewById<Button>(R.id.button_1).setOnClickListener {
            setNumber(1)
        }
        findViewById<Button>(R.id.button_2).setOnClickListener {
            setNumber(2)
        }
        findViewById<Button>(R.id.button_3).setOnClickListener {
            setNumber(3)
        }
        findViewById<Button>(R.id.button_4).setOnClickListener {
            setNumber(4)
        }
        findViewById<Button>(R.id.button_5).setOnClickListener {
            setNumber(5)
        }
        findViewById<Button>(R.id.button_6).setOnClickListener {
            setNumber(6)
        }
        findViewById<Button>(R.id.button_7).setOnClickListener {
            setNumber(7)
        }
        findViewById<Button>(R.id.button_8).setOnClickListener {
            setNumber(8)
        }
        findViewById<Button>(R.id.button_9).setOnClickListener {
            setNumber(9)
        }
        findViewById<Button>(R.id.button_plus).setOnClickListener {
            plus()
        }
        findViewById<Button>(R.id.button_minus).setOnClickListener {
            minus()
        }
        findViewById<Button>(R.id.button_divide).setOnClickListener {
            divide()
        }
        findViewById<Button>(R.id.button_multiply).setOnClickListener {
            multiply()
        }
        findViewById<Button>(R.id.button_factorial).setOnClickListener {
            factorial()
        }
        findViewById<Button>(R.id.button_pow).setOnClickListener {
            pow()
        }
        findViewById<Button>(R.id.button_sqrt).setOnClickListener {
            sqrt()
        }
        findViewById<Button>(R.id.button_percent).setOnClickListener {
            percent()
        }
        findViewById<Button>(R.id.button_point).setOnClickListener {
            point()
        }
        findViewById<Button>(R.id.button_clear).setOnClickListener {
            clear()
        }
        findViewById<Button>(R.id.button_clear_all).setOnClickListener {
            clear()
        }
        findViewById<ImageButton>(R.id.button_backspace).setOnClickListener {
            backspace()
        }
        findViewById<Button>(R.id.button_result).setOnClickListener {
            result()
        }
        findViewById<Button>(R.id.button_change_sign).setOnClickListener {
            changeSign()
        }
    }

    private fun display(currentLine: String) {
        progressBar?.progress = currentLine.length - 1
        currentTextView?.text = currentLine
    }

    private fun recentDisplay(recentLine: String) {
        recentTextView?.text = recentLine
    }

    private fun setNumber(number: Int) {
        currentLine = if (pointOn) {
            if (currentLine.endsWith(".0")) "${currentLine.toDouble().toInt()}.$number" else "${currentLine.toDouble()}$number"
        } else {
            if (currentLine.toDouble() == 0.0) "${number.toDouble()}" else "${currentLine.toDouble().toInt()}$number.0"
        }
        display(currentLine)
    }

    private fun clear() {
        currentLine = zero
        recentLine = ""
        a = 0.0
        b = 0.0
        signOn = false
        pointOn = false
        resultOn = false
        display(currentLine)
        recentDisplay(recentLine)
    }

    private fun backspace() {
        if (currentLine == zero) {
            pointOn = false
            signOn = false
            return
        } else if (currentLine == "") {
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
        display(currentLine)
    }

    private fun setSign(action: Actions) {
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
        recentDisplay(recentLine)
        display(currentLine)
    }

    fun plus() {
        setSign(Actions.PLUS)
    }

    fun minus() {
        setSign(Actions.MINUS)
    }

    private fun multiply() {
        setSign(Actions.MULTIPLY)
    }

    private fun divide() {
        setSign(Actions.DIVIDE)
    }

    private fun changeSign() {
        if (currentLine.toDouble() != 0.0) currentLine = "" + currentLine.toDouble() * -1
        display(currentLine)
    }

    private fun factorial() {
        if (currentLine.endsWith(".0") && currentLine != zero) {
            var result = 1
            for (i in 1..currentLine.toDouble().toInt()) result *= i
            currentLine = "$result.0"
            display(currentLine)
        }
    }

    private fun pow() {
        currentLine = "${currentLine.toDouble().pow(2.0)}"
        display(currentLine)
    }

    private fun sqrt() {
        currentLine = "${kotlin.math.sqrt(currentLine.toDouble())}"
        display(currentLine)
    }

    private fun point() {
        pointOn = true
    }

    private fun result() {
        if (currentAction == null) //отмена на случай, если не выбрано действие
            return
        calculateResult()
        resultOn = true
        pointOn = false
        signOn = false
        recentDisplay(recentLine)
        display(currentLine)
    }

    private fun calculateResult() { //вычисление результата
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

    private fun percent() { //вычисление процентов
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
        recentDisplay(recentLine)
        display(currentLine)
    }
}