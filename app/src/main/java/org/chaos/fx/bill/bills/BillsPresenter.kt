package org.chaos.fx.bill.bills

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.chaos.fx.bill.data.Bill
import org.chaos.fx.bill.data.BillsDataSource
import java.util.*

/**
 * @author Chaos
 *         18/05/2017
 */
class BillsPresenter(
        private val mView: BillsContract.View,
        private val mDataSource: BillsDataSource
) : BillsContract.Presenter {

    init {
        mView.setPresenter(this)
    }

    private val TAG = "BillsPresenter"

    private var mDisposables: CompositeDisposable? = null

    override fun subscribe() {
        mDisposables = CompositeDisposable()

        val cal = Calendar.getInstance()
        loadBillsByYearAndMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
    }

    override fun unsubscribe() {
        mDisposables!!.dispose()
    }

    override fun loadBillsByYearAndMonth(year: Int, month: Int) {
        val cal = Calendar.getInstance()
        cal.clear()
        cal.set(year, month, 1)
        val currentMonthFirstDayMillis = cal.timeInMillis

        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
        val nextMonthFirstDayMillis = cal.timeInMillis

        mDisposables!!.add(mDataSource.getBillsByTimes(currentMonthFirstDayMillis, nextMonthFirstDayMillis)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::notifyBillsLoaded,
                        {
                            Log.e(TAG, "loadBillsByYearAndMonth: ", it)
                            mView.showLoadingBillsError()
                        },
                        { mView.setLoadingIndicator(false) }))
    }

    private fun notifyBillsLoaded(bills: List<Bill>) {
        if (bills.isEmpty()) {
            mView.showNoBills()
        } else {
            mView.showBills(bills)
        }
    }

    override fun deleteBill(bill: Bill) {
        mDataSource.deleteBill(bill.id)
        mView.deleteBill(bill)
    }

    override fun updatedBill(id: Long) {
        mDataSource.getBillWithRefreshCache(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mView.updatedBill(it) },
                        { Log.e(TAG, "updatedBill: ", it) })
    }

    override fun addedBill(id: Long) {
        mDataSource.getBill(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mView.addedBill(it) },
                        { Log.e(TAG, "addedBill: ", it) })
    }
}