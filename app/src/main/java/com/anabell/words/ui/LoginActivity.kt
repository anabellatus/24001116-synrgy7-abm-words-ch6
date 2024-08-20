package com.anabell.words.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.anabell.words.R
import com.anabell.words.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val activityLoginBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels<LoginViewModel> {
        LoginViewModel.provideFactory(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityLoginBinding.root)

        activityLoginBinding.loginButton.setOnClickListener {
            if (activityLoginBinding.tieEmail.text.isNullOrEmpty()) {
                activityLoginBinding.tieEmail.error = getString(R.string.email_error)
            } else if (activityLoginBinding.tiePassword.text.isNullOrEmpty()) {
                activityLoginBinding.tiePassword.error = getString(R.string.password_error)
            } else if (activityLoginBinding.tiePassword.text!!.length < 8) {
                activityLoginBinding.tiePassword.error = getString(R.string.invalid_password)
            } else {
                activityLoginBinding.tieEmail.error = null
                activityLoginBinding.tiePassword.error = null

                activityLoginBinding.loginButton.isEnabled = false

                viewModel.login(
                    email = activityLoginBinding.tieEmail.text.toString(),
                    password = activityLoginBinding.tiePassword.text.toString()
                )
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                activityLoginBinding.loading.visibility = View.VISIBLE
            } else {
                activityLoginBinding.loading.visibility = View.GONE
            }
        }

        viewModel.success.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        viewModel.error.observe(this) { throwable ->
            Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
        }
    }
}