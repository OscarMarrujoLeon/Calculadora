package com.omarrujo.calculadora.mainModule.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.omarrujo.calculadora.databinding.ActivityMainBinding
import com.omarrujo.calculadora.mainModule.viewModel.OperacionesViewModel
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit  var operacionesViewModel: OperacionesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate( layoutInflater )
        setContentView(binding.root)

        operacionesViewModel = ViewModelProvider(this).get(OperacionesViewModel::class.java)
        binding.viewModel = operacionesViewModel
        binding.lifecycleOwner = this

    }


}