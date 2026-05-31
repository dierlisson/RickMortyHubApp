package com.dierlisson.rickmortyhub.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dierlisson.rickmortyhub.R
import com.dierlisson.rickmortyhub.ui.characterlist.CharacterListActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, CharacterListActivity::class.java))
            finish()
        }
    }
}
