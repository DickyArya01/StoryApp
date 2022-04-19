package com.dickyarya.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.dickyarya.storyapp.api.Api
import com.dickyarya.storyapp.databinding.ActivityRegisterBinding
import com.dickyarya.storyapp.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        showLoading(false)

        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etName.text.isEmpty()){
                    binding.etName.error = "Nama harus diisi"
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()){
                    binding.etEmail.error = "Invalid Email"
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etPassword.text.length <=5){
                    binding.etPassword.error = "Must be more than 6 Character"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.btnRegister.setOnClickListener {
            val name: String = binding.etName.text.toString()
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.toString()
            showLoading(true)

            actRegister(name, email, password)
        }

        binding.tvLoginIntent.setOnClickListener {
            moveIntentLogin()
        }
    }

    private fun actRegister(name: String, email: String, password: String){
        Api.getApi().register(
            name, email, password
        ).enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                when(response.code()){
                    201 -> {
                        moveIntentLogin()

                    }
                    400 -> {
                        showError("Data Salah atau Email sudah dibuat")
                    }
                    else -> {
                        showError("Terdapat masalah pada Server, silahkan tunggu")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showError("Server Error")
            }

        })
    }

    private fun moveIntentLogin(){
        Intent(this@RegisterActivity, LoginActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun showError(text: String){
        binding.tvError.text = text
        binding.tvError.visibility = View.VISIBLE
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar2.visibility = View.VISIBLE
        }else{
            binding.progressBar2.visibility = View.GONE
        }
    }
    private fun playAnimation(){
        val imageView = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(1000)
        val textLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(1000)
        val name = ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(1000)
        val btnLogin= ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(1000)
        val textRegister= ObjectAnimator.ofFloat(binding.lhRegister, View.ALPHA, 1f).setDuration(500)

        val title = AnimatorSet().apply {
            playTogether(imageView, textLogin)
        }

        val content = AnimatorSet().apply {
            playTogether(name, email, password, btnLogin)
        }

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        AnimatorSet().apply {
            playSequentially(title, content, textRegister)
            start()
        }
    }
}