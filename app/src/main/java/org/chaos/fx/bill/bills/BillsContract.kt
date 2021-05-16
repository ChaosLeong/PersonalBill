package org.chaos.fx.bill.bills

import org.chaos.fx.bill.BasePresenter
import org.chaos.fx.bill.BaseView
import org.chaos.fx.bill.data.Bill

/**
 * @author Chaos
 *         18/05/2017
 */

interface BillsContract {
    interface Presenter : BasePresenter {
        fun loadBillsByYearAndMonth(year: Int, month: Int)
        fun deleteBill(bill: Bill)
        fun updatedBill(id: Long)
        fun addedBill(id: Long)
    }

    interface View : BaseView<Presenter> {
        fun showBills(bill: List<Bill>)
        fun deleteBill(bill: Bill)
        fun updatedBill(bill: Bill)
        fun addedBill(bill: Bill)
        fun showLoadingBillsError()
        fun setLoadingIndicator(boolean: Boolean)
        fun showNoBills()
    }
}