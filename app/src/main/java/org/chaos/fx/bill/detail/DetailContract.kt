package org.chaos.fx.bill.detail

import org.chaos.fx.bill.BasePresenter
import org.chaos.fx.bill.BaseView

/**
 * @author Chaos
 *         19/05/2017
 */
interface DetailContract {

    interface View : BaseView<Presenter> {
        fun showBillType(isIncome: Boolean)
        fun showCategory(category: String)
        fun showPrice(price: Float)
        fun showDate(timestamp: Long)
        fun showNote(note: String)
    }

     interface Presenter : BasePresenter {
        fun saveBill(isIncome: Boolean, type: String, sum: Float, timestamp: Long, note: String): Long
    }
}