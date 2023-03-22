package com.example.robotjoystick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.robotjoystick.databinding.ActivityMainBinding
import com.example.robotjoystick.view.knowndevices.KnownBluetoothDevicesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView, KnownBluetoothDevicesFragment())
            .commit()
    }
}