package com.omarrujo.calculadora.mainModule.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OperacionesViewModel(  ) : ViewModel() {

    //HISTORY SCREEN
    private val _resultHistory = MutableLiveData<String>().apply {
        value = ""
    }
    val resultHistory: LiveData<String> get() = _resultHistory

    //ACTUALLY SCREEN
    private val _result = MutableLiveData<String>().apply {
        value = "0"
    }
    val result: LiveData<String> get() = _result

    //HISTORY SIZE
    private val _textSizeHistory = MutableLiveData<Float>().apply {
        value = 120.0f
    }
    val textSizeHistory: LiveData<Float> get() = _textSizeHistory

    //ACTUALLY SIZE
    private val _textSizeActually = MutableLiveData<Float>().apply {
        value = 80.0f
    }
    val textSizeActually: LiveData<Float> get() = _textSizeActually

    //OPACITY
    private val _opacity = MutableLiveData<Float>().apply {
        value = 1.0f
    }
    val opacity: LiveData<Float> get() = _opacity



    private val currentInput = StringBuilder( "0" )
    private var operand1 = 0.0
    private var operator = ' '
    private var resultValue = 0.0
    private var anotherOperation = false

    fun onDigitButtonClick(digit: Int) {
        onChangeSizeButtonClick( 2 )
        _opacity.value = 0.8f

        if ( _result.value.equals("0") ) {
            _result.value = digit.toString()
        }
        else {
            if (!anotherOperation)
                _result.value += digit.toString()
        }
        _resultHistory.value += digit

        currentInput.append(digit)
        if ( !operator.toString().trim().isEmpty() ) onPerformOperation()

    }

    fun onOperatorButtonClick( op: Char ) {
        onChangeSizeButtonClick( 2 )

        if ( anotherOperation )  operand1 = resultValue else operand1 = currentInput.toString().toDouble()

        operator = op
        if ( operand1.equals(0.0) ) {
            _resultHistory.value = "${formatNumber(operand1)}$operator"
        }
        _resultHistory.value += operator
        anotherOperation = true
        currentInput.setLength( 0 )
    }

    fun onPerformOperation() {
        try {
            val operand2 = getLastNumber( resultHistory.value.toString() )

            when (operator) {
                '+' -> resultValue = operand1 + operand2
                '-' -> resultValue = operand1 - operand2
                '÷' -> resultValue = operand1 / operand2
                '×' -> resultValue = operand1 * operand2
                '%' -> resultValue = ( operand2 / operand1 ) * 100
            }

            _result.value = formatNumber( resultValue )

        } catch (e: Exception) {
           _result.value = "Error"
        }
    }

    fun onDeleteScreenButtonClick(option: Int) {
        when (option) {
            1 -> resetToDefaultState()
            2 -> resetToDefaultState()
        }
    }

    private fun resetToDefaultState() {
        currentInput.setLength(0)
        currentInput.append("0")
        _resultHistory.value = ""
        _result.value = "0"
        operand1 = 0.0
        operator = ' '
        _opacity.value = 1.0f
        resultValue = 0.0
        anotherOperation = false
    }

    private fun clearLastCharacter() {

        if ( !_result.value.isNullOrEmpty() ) {
            _result.value = _result.value?.dropLast(1)
            _resultHistory.value = _resultHistory.value?.dropLast(1)
            _result.value = "0"
            resultValue = 0.0

            anotherOperation = false
        }

    }

    fun onPutPointButtonClick(){
        if( !_result.value?.contains(".")!! == true || anotherOperation) {
            if ( _result.value.equals("0") ) {
                if ( !anotherOperation ){
                    _result.value = "0."
                }
                _resultHistory.value = "0."
                currentInput.append("0.")
            } else{
                if ( !anotherOperation ){
                    _result.value += "."
                }
                _resultHistory.value += "."
                currentInput.append(".")
            }
        }
    }
    fun onChangeSizeButtonClick( option: Int ){
        when ( option ){
            1 -> { //SHOW RESULT EQUAL
                _textSizeHistory.value = 80.0f
                _textSizeActually.value =  120.0f
                _opacity.value = 1.0f
            }
            2 -> { // RETURN TO THE ORIGINAL
                _textSizeHistory.value = 120.0f
                _textSizeActually.value =  80.0f
                _opacity.value = 0.7f
            }
        }
    }

    private fun formatNumber( number: Double ): String {
       // val formatted = String.format("%.12f", number).trimEnd('0', '.')
        return  String.format("%.2f", number)
    }

    fun getLastNumber(cadena: String): Double {
        val regex = Regex("\\d+\\.?\\d*")
        val numbers = regex.findAll(cadena).map { it.value }.toList()
        return numbers.last().toDouble()

    }
}