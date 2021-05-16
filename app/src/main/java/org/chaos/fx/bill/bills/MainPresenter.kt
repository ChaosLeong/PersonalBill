package org.chaos.fx.bill.bills

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.chaos.fx.bill.data.Bill
import org.chaos.fx.bill.data.BillsDataSource
import org.chaos.fx.bill.detail.BillDetailActivity
import java.util.*

/**
 * @author Chaos
 *         19/05/2017
 */
class MainPresenter(
        private val mView: MainContract.View,
        private val mDataSource: BillsDataSource
) : MainContract.Presenter, BillsFragment.OnBillsChangedListener {

    private val TAG = "MainPresenter"

    private var mDisposable: Disposable? = null

    private var mCacheBills: MutableList<Bill>? = null
    private var mCacheExpense: Float = 0F
    private var mCacheIncome: Float = 0F

    override fun subscribe() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        updateHeaderData(year, month)
    }

    override fun unsubscribe() {
        mDisposable?.dispose()
    }

    override fun updateHeaderData(year: Int, month: Int) {
        val cal = Calendar.getInstance()
        cal.clear()
        cal.set(year, month, 1)
        mView.updateHeaderDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
        val currentMonthFirstDayMillis = cal.timeInMillis

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
        val nextMonthFirstDayMillis = cal.timeInMillis

        mDisposable = mDataSource.getBillsByTimes(currentMonthFirstDayMillis, nextMonthFirstDayMillis)
                .map(this::processBills)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ updateIncomeAndExpense() })
    }

    private fun processBills(bills: List<Bill>) {
        mCacheBills = bills.toMutableList()

        mCacheExpense = 0F
        mCacheIncome = 0F

        for (bill in bills) {
            if (bill.isIncome) {
                mCacheIncome += bill.price
            } else {
                mCacheExpense += bill.price
            }
        }
    }

    private fun updateIncomeAndExpense() {
        mView.updateHeaderExpense(mCacheExpense)
        mView.updateHeaderIncome(mCacheIncome)
    }

    override fun onBillDeleted(bill: Bill) {
        mCacheBills?.remove(bill)
    }

    override fun onBillUpdated(bill: Bill) {
        val index = mCacheBills?.indexOf(bill) ?: -1
        if (index >= 0) {
            val oldBill = mCacheBills?.get(index)
            oldBill?.let {
                mCacheBills?.set(index, bill)
                val oldPrice = it.price
                val newPrice = bill.price
                if (it.isIncome) {
                    mCacheIncome -= oldPrice
                } else {
                    mCacheExpense -= oldPrice
                }
                if (bill.isIncome) {
                    mCacheIncome += newPrice
                } else {
                    mCacheExpense += newPrice
                }
            }
        }
        updateIncomeAndExpense()
    }

    override fun onBillAdded(bill: Bill) {
        mCacheBills?.add(0, bill)
        if (bill.isIncome) {
            mCacheIncome += bill.price
        } else {
            mCacheExpense += bill.price
        }
        updateIncomeAndExpense()
    }
}