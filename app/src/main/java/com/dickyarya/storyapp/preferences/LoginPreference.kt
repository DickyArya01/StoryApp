package com.dickyarya.storyapp.preferences

import android.content.Context

internal class LoginPreference(context: Context) {

    companion object{
        const val PREFS_NAME = "login_pref"
        const val USERNAME= "username"
        const val EMAIL= "email"
        const val TOKEN = "token"
        const val IS_LOGIN= "login"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun put(key: String, value: String){
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun put(key: String, value: Boolean){
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean? {
        return preferences.getBoolean(key, false)
    }

    fun logOut(){
        editor.clear()
        editor.apply()
    }

}