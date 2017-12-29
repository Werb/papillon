package com.werb.papillon

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatTextView
import android.text.Html
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView
import com.werb.papillon.extension.string
import com.werb.papillon.model.User
import com.werb.papillon.persistence.OAuthUserViewModel
import com.werb.papillon.repository.UserRepository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.widget_view_search.*

class MainActivity : AppCompatActivity() {

    private lateinit var oauthUserViewModel: OAuthUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
        subscribeUI()
    }

    private fun initUI() {
        // toolbar
        setSupportActionBar(searchToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarTitle.text = string(R.string.app_name)
        val toggle = ActionBarDrawerToggle(this, mainDrawerLayout, searchToolbar, R.string.open, R.string.close)
        mainDrawerLayout.setDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initUser(user: User) {
        val headerView = mainDrawer.getHeaderView(0)
        headerView.findViewById<SimpleDraweeView>(R.id.avatar).setImageURI(user.avatar_url)
        headerView.findViewById<AppCompatTextView>(R.id.name).text = user.name
        headerView.findViewById<AppCompatTextView>(R.id.bio).text = Html.fromHtml(user.bio)
    }

    private fun subscribeUI() {
        oauthUserViewModel = ViewModelProviders.of(this, OAuthUserViewModel.Factory(UserRepository())).get(OAuthUserViewModel::class.java)
        oauthUserViewModel.oaothUser?.observe(this, Observer { user ->
            user?.let {
                initUser(it)
            }
        })
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            activity.finish()
        }
    }
}
