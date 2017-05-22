package org.chaos.fx.bill.bills

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.item_bill.view.*
import org.chaos.fx.bill.R
import org.chaos.fx.bill.data.Bill
import java.util.*

/**
 * @author Chaos
 *         18/05/2017
 */

class BillHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val sCalendar = Calendar.getInstance()!!
    }

    @SuppressLint("SetTextI18n")
    fun bindData(bill: Bill, preBill: Bill?) {
        itemView.msg.text = if (TextUtils.isEmpty(bill.note)) {
            bill.category
        } else {
            String.format("%1\$s-%2\$s", bill.category, bill.note)
        }

        itemView.price.text = if (bill.isIncome) {
            "+"
        } else {
            "-"
        } + bill.price

        itemView.type_img.setImageResource(if (bill.isIncome) {
            R.drawable.income
        } else {
            R.drawable.expend
        })

        setupDate(bill.timestamp, preBill?.timestamp ?: -1)
    }

    private fun setupDate(timestamp: Long, preTimestamp: Long) {
        itemView.date.visibility = VISIBLE

        sCalendar.timeInMillis = timestamp

        val field = Calendar.DAY_OF_MONTH
        val text = DateFormat.format(itemView.resources.getString(R.string.day_of_month_format), sCalendar)

        itemView.date.text = text

        if (preTimestamp > 0) {
            val curTime = sCalendar.get(field)
            val preTime = {
                sCalendar.timeInMillis = preTimestamp
                sCalendar.get(field)
            }

            if (curTime == preTime.invoke()) {
                itemView.date.visibility = GONE
            }
        }
    }
}