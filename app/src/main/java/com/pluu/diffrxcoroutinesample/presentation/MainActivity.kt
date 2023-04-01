package com.pluu.diffrxcoroutinesample.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pluu.diffrxcoroutinesample.databinding.ActivityMainBinding
import logcat.logcat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViews()
        setUpObserves()
    }

    private fun setUpViews() {
        binding.btnRxNetworkError.setOnClickListener {
            viewModel.tryRxNetworkError()
        }
        binding.btnRxViewModelError.setOnClickListener {
            viewModel.tryRxViewModelError()
        }
        binding.btnRxLiveDataError.setOnClickListener {
            viewModel.tryRxLiveDataError()
        }

        binding.btnCoroutineNetworkError.setOnClickListener {
            viewModel.tryCoroutineNetworkError()
        }
        binding.btnCoroutineViewModelError.setOnClickListener {
            viewModel.tryCoroutineViewModelError()
        }
        binding.btnCoroutineLiveDataError.setOnClickListener {
            viewModel.tryCoroutineLiveDataError()
        }
    }

    private fun setUpObserves() {
        viewModel.showErrorBundle.observe(this) {
            logcat { "Error Receive = ${it.throwable.message}" }
        }
        viewModel.userEvent.observe(this) { success ->
            logcat { "Receive: $success" }
            throw IllegalStateException("Force exception on UI")
        }
    }
}