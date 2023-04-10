package com.example.robotjoystick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.robotjoystick.databinding.ActivityMainBinding
import com.example.robotjoystick.view.knowndevices.KnownDevicesScreen
import com.example.robotjoystick.view.scandevices.ScanDevicesScreen
import com.example.robotjoystick.view.terminal.TerminalScreen
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    @Inject
    lateinit var router: Router

    private lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigator = AppNavigator(this, R.id.fragment_container_view)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == binding.bottomNavigation.selectedItemId) {
                return@setOnItemSelectedListener false
            }
            return@setOnItemSelectedListener updateRootFragment(item.itemId)
        }

        if (savedInstanceState == null) {
            updateRootFragment(R.id.menu_item_known_devices)
        }
    }

    private fun updateRootFragment(id: Int): Boolean {
        val screen = when (id) {
                R.id.menu_item_known_devices -> KnownDevicesScreen()
                R.id.menu_item_scan_devices -> ScanDevicesScreen()
                R.id.menu_item_terminal -> TerminalScreen()
                R.id.menu_item_settings -> return false // todo
                else -> return false
            }
        router.newRootScreen(screen)
        return true
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}