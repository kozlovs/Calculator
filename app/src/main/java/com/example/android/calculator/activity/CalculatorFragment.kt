package com.example.android.calculator.activity

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.calculator.R
import com.example.android.calculator.databinding.FragmentCalculatorBinding
import com.example.android.calculator.viewmodel.CalkViewModel

class CalculatorFragment : Fragment() {
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var binding: FragmentCalculatorBinding
    private val viewModel: CalkViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        clipboardManager = container?.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        setListeners()
        updateView()
        return binding.root
    }

    private fun setListeners() {
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

            buttonCopy.setOnClickListener {
                clipboardManager.setPrimaryClip(viewModel.getClipData())
                Toast
                    .makeText(context, getString(R.string.copied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateView() {
        viewModel.display.observe(viewLifecycleOwner) {
            binding.progressBar.progress = it.length - 1
            binding.currentDisplayTextView.setText(it)
        }
        viewModel.history.observe(viewLifecycleOwner) {
            binding.recentDisplayTextView.text = it
        }
    }
}