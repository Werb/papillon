package com.werb.papillon.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.werb.library.MoreAdapter
import com.werb.library.link.RegisterItem
import com.werb.papillon.R
import com.werb.papillon.persistence.ExploreShotsViewModel
import com.werb.papillon.repository.ShotsRepository
import com.werb.papillon.widget.card.GridFeedViewHolder
import com.werb.papillon.widget.diff.ShotsDiff
import kotlinx.android.synthetic.main.fragment_explore_feed.*

/**
 * Created by wanbo on 2017/12/29.
 */
class ExploreFeedFragment : Fragment() {

    private lateinit var shotsViewModel: ExploreShotsViewModel
    private val adapter = MoreAdapter()
    private var load = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_explore_feed, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        refresh.setColorSchemeResources(R.color.color_EA4C89)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        (recyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        adapter.apply {
            userSoleRegister()
            register(RegisterItem(R.layout.layout_view_grid_feed, GridFeedViewHolder::class.java))
            attachTo(recyclerView)
        }
        refresh.setProgressViewEndTarget(true, 135)
        refresh.setOnRefreshListener {
            load = true
            shotsViewModel.refresh()
        }
    }

    private fun subscribeUI() {
        if (!isAdded) return
        shotsViewModel = ViewModelProviders.of(this, ExploreShotsViewModel.Factory(ShotsRepository())).get(ExploreShotsViewModel::class.java)
        shotsViewModel.shots.observe(this, Observer { shots ->
            shots?.let {
                if (load) {
                    adapter.refresh(0, it, ShotsDiff::class.java)
                } else {
                    adapter.loadData(it)
                }
            }
        })
        shotsViewModel.loading.observe(this, Observer { loading ->
            if (!isHidden) {
                loading?.let {
                    refresh.isRefreshing = loading
                }
            }
        })
        shotsViewModel.setRequestConfig(sort = arguments?.getString("type"))
        shotsViewModel.refresh()
    }

    companion object {

        fun newInstance(type: String): ExploreFeedFragment = ExploreFeedFragment().apply {
            val args = Bundle()
            args.putString("type", type)
            arguments = args
        }

    }

}