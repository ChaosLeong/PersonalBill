package org.chaos.fx.bill.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.chaos.fx.bill.data.Bill
import org.chaos.fx.bill.data.BillsDataSource

/**
 * @author Chaos
 *         19/05/2017
 */
class BillDetailPresenter(
        private val mId: Long? = null,
        private val mView: DetailContract.View,
        private val mDateSource: BillsDataSource
) : DetailContract.Presenter {

    override fun subscribe() {
        showBill()
    }

    override fun unsubscribe() {

    }

    private fun showBill() {
        mId?.let {
            mDateSource.getBill(it)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::processBill)
        }
    }

    private fun processBill(bill: Bill?) {
        bill?.let {
            mView.showBillType(bill.isIncome)
            mView.showCategory(bill.category)
            mView.showNote(bill.note)
            mView.showPrice(bill.price)
            mView.showDate(bill.timestamp)
        }
    }

    override fun saveBill(isIncome: Boolean, type: String, sum: Float, timestamp: Long, note: String): Long {
        return mDateSource.saveBill(Bill(mId, isIncome, type, sum, timestamp, note))
    }

}