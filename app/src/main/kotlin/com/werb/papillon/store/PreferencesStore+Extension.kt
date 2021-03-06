package com.werb.papillon.store

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import com.werb.papillon.MyApp
import com.werb.papillon.model.data.Token
import com.werb.papillon.model.data.User

/**
 * Created by wanbo on 2017/12/25.
 */
object PreferencesStore {

    private val mSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApp.instance)

    private val TOKEN = "token"
    private val USER = "user"
    private val FEED = "feed"

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

    var user: User?
        get() {
            val str = mSharedPreferences.getString(USER, null)
            return if (TextUtils.isEmpty(str)) {
                null
            } else {
                Gson().fromJson(str, User::class.java)
            }
        }
        set(value) {
            mSharedPreferences.string(USER, Gson().toJson(value))
        }

    var feed: FeedUI
        get() {
            val str = mSharedPreferences.getString(FEED, FeedUI.GRID.name)
            return FeedUI.valueOf(str)
        }
        set(value) {
            mSharedPreferences.string(FEED, value.name)
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