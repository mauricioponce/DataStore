package cl.eme.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cl.eme.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*
Preference Manager using DataStore from Jetpack

https://developer.android.com/topic/libraries/architecture/datastore

This examples is based on https://medium.com/better-programming/jetpack-datastore-improved-data-storage-system-adec129b6e48
 */
class MainActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        collectStatusFromPref()

        initListeners()
    }

    private fun initListeners() {
        binding.btStarted.setOnClickListener { saveUserStatus(1) }
        binding.btLevel1.setOnClickListener { saveUserStatus(2) }
        binding.btLevel2.setOnClickListener { saveUserStatus(3) }
        binding.btVerified.setOnClickListener { saveUserStatus(4) }
    }

    private fun collectStatusFromPref() {
        lifecycleScope.launch {
            prefManager.userStatusFlow.collectLatest {
                binding.textView.text = when(it) {
                    UserStatus.STARTED -> "started"
                    UserStatus.LEVEL_1 -> "l 1"
                    UserStatus.LEVEL_2 -> "l 2"
                    UserStatus.VERIFIED -> "verified"
                }
            }
        }
    }

    private fun saveUserStatus(status: Int) {
        lifecycleScope.launch {
            when(status) {
                1 -> prefManager.setUserStatus(UserStatus.STARTED)
                2 -> prefManager.setUserStatus(UserStatus.LEVEL_1)
                3 -> prefManager.setUserStatus(UserStatus.LEVEL_2)
                4 -> prefManager.setUserStatus(UserStatus.VERIFIED)
            }
        }
    }
}