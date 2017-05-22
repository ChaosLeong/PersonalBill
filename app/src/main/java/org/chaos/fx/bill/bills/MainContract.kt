package org.chaos.fx.bill.bills

import org.chaos.fx.bill.BasePresenter
import org.chaos.fx.bill.BaseView

/**
 * @author Chaos
 *         19/05/2017
 */
interface MainContract {

    interface Presenter : BasePresenter {
        fun updateHeaderData(year: Int, month: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateHeaderDate(year: Int, month: Int)
        fun updateHeaderIncome(income: Float)
        fun updateHeaderExpense(expense: Float)
    }
}