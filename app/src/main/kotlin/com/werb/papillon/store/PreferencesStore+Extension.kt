package com.werb.papillon.store

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import com.werb.papillon.MyApp
import com.werb.papillon.model.Token

/**
 * Created by wanbo on 2017/12/25.
 */
object PreferencesStore {

    private val mSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.instance)

    private val TOKEN = "token"

    var token: Token?
        get() {
            val str = mSharedPreferences.getString(TOKEN, null)
            return if (TextUtils.isEmpty(str)) {
                null
            } else {
                Gson().fromJson(str, Token::class.java)
            }
        }
        set(value) {
            mSharedPreferences.string(TOKEN, Gson().toJson(value))
        }


}

fun SharedPreferences.string(key: String, value: String): Boolean {
    val editor = edit()
    editor.putString(key, value)
    return editor.commit()
}

fun SharedPreferences.int(key: String, value: Int): Boolean {
    val editor = edit()
    editor.putInt(key, value)
    return editor.commit()
}