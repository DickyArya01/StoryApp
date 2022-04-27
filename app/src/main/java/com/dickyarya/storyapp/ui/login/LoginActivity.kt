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
import com.dickyarya.storyapp.databinding.ActivityLoginBinding
import com.dickyarya.storyapp.model.LoginResponse
import com.dickyarya.storyapp.preferences.LoginPreference
import com.dickyarya.storyapp.ui.main.StoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var preference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        preference = LoginPreference(this)

        binding.etEmail.addTextChangedListener(object : TextWatcher{
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
        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.etPassword.text!!.length <=5){
                    binding.etPassword.error = "Must be more than 6 Character"
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()
            showLoading(true)
            actLogin(email, password)

        }
        
        binding.tvRegisterIntent.setOnClickListener {
            moveIntentRegister()
        }
    }

    override fun onStart() {
        super.onStart()
        if (preference.getBoolean(LoginPreference.IS_LOGIN) == true){

            moveIntentStory()
            finish()
        }
    }

    private fun actLogin(email: String, password: String) {
       Api.getApi().login(
           email, password
       ).enqueue(object : Callback<LoginResponse>{
           override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
               showLoading(false)
               when(response.code()){
                   200 -> {
                       val name: String = response.body()?.loginResult!!.name
                       val token: String = response.body()?.loginResult!!.token
                       saveSession(email, name, token)
                       moveIntentStory()
                   }
                   400 -> {
                       showError("Tolong masukkan email dan password dengan benar")
                   }
                   401 -> {
                       showError("Email atau password salah")
                   }
                   else -> {
                       showError("Terdapat masalah pada Server, silahkan tunggu")
                   }
               }

           }

           override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
              showError("Server Or Connection to Server Error")
           }

       })
    }

    private fun moveIntentStory(){
        Intent(this@LoginActivity, StoryActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun moveIntentRegister(){
        Intent(this@LoginActivity, RegisterActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun saveSession(email: String, name: String, token: String){
        preference.put(LoginPreference.EMAIL, email)
        preference.put(LoginPreference.USERNAME, name)
        preference.put(LoginPreference.TOKEN, token)
        preference.put(LoginPreference.IS_LOGIN, true)
    }



    private fun showError(text: String){
        binding.tvError.text = text
        binding.tvError.visibility = View.VISIBLE
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun playAnimation(){
        val imageView = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(1000)
        val textLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(1000)
        val email = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(1000)
        val password = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(1000)
        val btnLogin= ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val textRegister= ObjectAnimator.ofFloat(binding.lhText, View.ALPHA, 1f).setDuration(500)

        val title = AnimatorSet().apply {
            playTogether(imageView, textLogin)
        }

        val content = AnimatorSet().apply {
            playTogether(email, password, btnLogin)
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