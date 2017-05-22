package org.chaos.fx.bill.bills

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.chaos.fx.bill.R
import org.chaos.fx.bill.data.Bill
import java.util.*

/**
 * @author Chaos
 *         18/05/2017
 */
class BillAdapter : RecyclerView.Adapter<BillHolder>() {

    var mBills: MutableList<Bill> = Collections.emptyList<Bill>()

    var mItemClickListener: OnItemClickListener? = null
    private val mInternalOnClickListener: View.OnClickListener = View.OnClickListener {
        mItemClickListener?.onItemClick(it)
    }

    var mItemLongClickListener: OnItemLongClickListener? = null
    val mInternalItemLongClickListener: View.OnLongClickListener = View.OnLongClickListener {
        mItemLongClickListener?.onItemLongClick(it)
        true
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BillHolder {
        val holder = BillHolder(LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_bill, parent, false))
        holder.itemView.setOnClickListener(mInternalOnClickListener)
        holder.itemView.setOnLongClickListener(mInternalItemLongClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: BillHolder?, position: Int) {
        val preItem = if (position == 0) null else mBills[position - 1]
        holder!!.bindData(mBills[position], preItem)
    }

    override fun getItemCount(): Int {
        return mBills.size
    }

    interface OnItemClickListener {
        fun onItemClick(v: View)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(v: View)
    }
}