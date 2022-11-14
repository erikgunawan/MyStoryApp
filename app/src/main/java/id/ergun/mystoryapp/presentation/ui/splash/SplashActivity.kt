package id.ergun.mystoryapp.presentation.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import id.ergun.mystoryapp.databinding.ActivitySplashBinding
import id.ergun.mystoryapp.presentation.ui.auth.login.LoginActivity

/**
 * @author erikgunawan
 * Created 15/11/22 at 05.41
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed({
            gotoHomeActivity()
        }, 1000)
    }

    private fun gotoHomeActivity() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

}