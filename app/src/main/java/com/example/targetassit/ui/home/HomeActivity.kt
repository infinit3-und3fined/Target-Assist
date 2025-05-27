package com.example.targetassit.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.targetassit.R
import com.example.targetassit.data.repository.DeviceInfoRepository

class HomeActivity : AppCompatActivity() {
    
    private lateinit var viewModel: HomeViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        // Initialize device info
        val deviceInfoRepository = DeviceInfoRepository(this)
        val deviceInfo = deviceInfoRepository.getDeviceInfo()
        
        // We'll implement UI updates later
    }
} 