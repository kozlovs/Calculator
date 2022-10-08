package com.example.android.calculator.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.calculator.databinding.FragmentCalculatorBinding
import com.example.android.calculator.viewmodel.CalkViewModel

class CalculatorFragment : Fragment() {


    private val viewModel: CalkViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    lateinit var display: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        initViews(binding)
        return binding.root
    }

    private fun initViews(binding: FragmentCalculatorBinding) {
        binding.apply {
            button0.setOnClickListener { viewModel.setNumber(0) }
            button1.setOnClickListener { viewModel.setNumber(1) }
            button2.setOnClickListener { viewModel.setNumber(2) }
            button3.setOnClickListener { viewModel.setNumber(3) }
            button4.setOnClickListener { viewModel.setNumber(4) }
            button5.setOnClickListener { viewModel.setNumber(5) }
            button6.setOnClickListener { viewModel.setNumber(6) }
            button7.setOnClickListener { viewModel.setNumber(7) }
            button8.setOnClickListener { viewModel.setNumber(8) }
            button9.setOnClickListener { viewModel.setNumber(9) }

            buttonPlus.setOnClickListener { viewModel.plus() }
            buttonMinus.setOnClickListener { viewModel.minus() }
            buttonDivide.setOnClickListener { viewModel.divide() }
            buttonMultiply.setOnClickListener { viewModel.multiply() }

            buttonPow.setOnClickListener { viewModel.pow() }
            buttonFactorial.setOnClickListener { viewModel.factorial() }
            buttonSqrt.setOnClickListener { viewModel.sqrt() }
            buttonPercent.setOnClickListener { viewModel.percent() }
            buttonChangeSign.setOnClickListener { viewModel.changeSign() }
            buttonPoint.setOnClickListener { viewModel.point() }

            buttonClear.setOnClickListener { viewModel.clear() }
            buttonClearAll.setOnClickListener { viewModel.clear() }
            buttonBackspace.setOnClickListener { viewModel.backspace() }
            buttonResult.setOnClickListener { viewModel.result() }

            buttonCopy.setOnClickListener { viewModel.copy() }
        }

        viewModel.display.observe(viewLifecycleOwner) {
            display = it
            binding.progressBar.progress = display.length - 1
            binding.currentDisplayTextView.setText(it)
        }
    }
}