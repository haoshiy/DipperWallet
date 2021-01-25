package com.highstreet.wallet.ui.vm

import androidx.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.RxBus
import com.highstreet.wallet.AccountManager
import com.highstreet.wallet.event.RefreshDelegationEvent
import com.highstreet.wallet.http.ApiService
import com.highstreet.wallet.http.subscribeBy
import com.highstreet.wallet.model.req.RequestBroadCast
import com.highstreet.wallet.model.res.AccountInfo
import com.highstreet.wallet.utils.AmountUtils
import com.highstreet.wallet.crypto.KeyUtils
import com.highstreet.wallet.utils.MsgGeneratorUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class DelegationVM : BalanceVM() {

    val resultLD = MutableLiveData<Pair<Boolean, String>>()

    fun delegate(validationAddress: String, toAmount: String, remarks: String) {
        ApiService.getDipApi().account(AccountManager.instance().address).subscribeBy({
            val coins = it.result?.value?.coins
            if (null != coins && coins.isNotEmpty()) {
                val balance = coins[0].amount ?: "0"
                if (isEnough(balance, toAmount)) {
                    generateParams(it.result!!, validationAddress, toAmount, remarks)
                } else {
                    resultLD.value = Pair(false, "余额不足")
                }
            }
        }, {
            resultLD.value = Pair(false, "委托失败")
        }).add()
    }

    private fun isEnough(balance: String, toAmount: String): Boolean {
        return balance.toLong() > toAmount.toLong()
    }

    private fun generateParams(
        accountInfo: AccountInfo,
        validationAddress: String,
        toAmount: String,
        remarks: String
    ) {
        val account = AccountManager.instance().account!!
        account.accountNumber = accountInfo.getAccountNumber()
        account.sequenceNumber = accountInfo.getSequence()
        val deterministicKey =
            KeyUtils.getDeterministicKey(account.chain, account.getEntropyAsHex(), account.path)
        val msg = MsgGeneratorUtils.delegateMsg(
            account.address,
            validationAddress,
            AmountUtils.generateCoin(toAmount),
            account.chain
        )
        doDelegate(
            MsgGeneratorUtils.getBroadCast(
                account,
                msg,
                AmountUtils.generateFee(),
                remarks,
                deterministicKey
            )
        )
    }

    private fun doDelegate(reqBroadCast: RequestBroadCast) {
        ApiService.getDipApi().txs(reqBroadCast).subscribeBy({
            if (it.success()) {
                RxBus.instance().send(RefreshDelegationEvent())
                resultLD.value = Pair(true, "委托成功")
            } else {
                resultLD.value = Pair(false, "委托失败")
            }

        }, {
            resultLD.value = Pair(false, it)
        }).add()
    }
}