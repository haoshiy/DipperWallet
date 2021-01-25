package com.highstreet.wallet.ui.fragment

import android.view.View
import com.highstreet.lib.ui.BaseListFragment
import com.highstreet.wallet.R
import com.highstreet.wallet.model.res.Proposal
import com.highstreet.wallet.ui.activity.ProposalDetailActivity
import com.highstreet.wallet.ui.adapter.ProposalAdapter
import com.highstreet.wallet.ui.vm.ProposalVM


/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */

class ProposalFragment : BaseListFragment<Proposal, ProposalVM>() {

    override fun getLayoutId() = R.layout.g_fragment_proposal

    override fun createAdapter() = ProposalAdapter()

    override fun itemClicked(view: View, item: Proposal, position: Int) {
        activity?.apply {  ProposalDetailActivity.start(this, item) }
    }
}