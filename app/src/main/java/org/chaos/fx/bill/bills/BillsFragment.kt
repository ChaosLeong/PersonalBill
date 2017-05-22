package org.chaos.fx.bill.bills

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.HORIZONTAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.fragment_bill.*
import kotlinx.android.synthetic.main.layout_no_data.*
import org.chaos.fx.bill.R
import org.chaos.fx.bill.data.Bill
import org.chaos.fx.bill.detail.BillDetailActivity


/**
 * @author Chaos
 *         18/05/2017
 */

class BillsFragment : Fragment(), BillsContract.View {

    companion object {
        val REQUEST_CODE_ADD_OR_EDIT = 1
    }

    private var mPresenter: BillsContract.Presenter? = null
    private var mAdapter: BillAdapter? = null

    private var mBillsChangedListener: OnBillsChangedListener? = null

    private var mIdxForDisplayedMenu: Int = 0
    private var mEditingBill: Boolean = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_bill, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        details.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager
        details.addItemDecoration(DividerItemDecoration(activity, HORIZONTAL))
        mAdapter = BillAdapter()
        mAdapter!!.mItemClickListener = object : BillAdapter.OnItemClickListener {
            override fun onItemClick(v: View) {
                mEditingBill = true
                val position = details!!.getChildAdapterPosition(v)
                val id = mAdapter!!.mBills[position].id
                BillDetailActivity.startForResult(this@BillsFragment, REQUEST_CODE_ADD_OR_EDIT, id)
            }
        }
        mAdapter!!.mItemLongClickListener = object : BillAdapter.OnItemLongClickListener {
            override fun onItemLongClick(v: View) {
                val position = details!!.getChildAdapterPosition(v)
                mIdxForDisplayedMenu = position
                details.showContextMenuForChild(v)
            }
        }
        details.adapter = mAdapter
        registerForContextMenu(details)

        mPresenter?.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.unsubscribe()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity.menuInflater.inflate(R.menu.menu_bill_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.delete) {
            mAdapter!!.mBills.removeAt(mIdxForDisplayedMenu)
            mAdapter!!.notifyItemRemoved(mIdxForDisplayedMenu)
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_add_bill -> {
                BillDetailActivity.startForResult(this@BillsFragment, BillsFragment.REQUEST_CODE_ADD_OR_EDIT, null)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ADD_OR_EDIT) {
            data?.let {
                val id = data.getLongExtra(BillDetailActivity.KEY_BILL_ID, -1)
                if (mEditingBill) {
                    mPresenter?.updatedBill(id)
                    mEditingBill = false
                } else {
                    mPresenter?.addedBill(id)
                }
            }
        }
    }

    override fun setPresenter(presenter: BillsContract.Presenter) {
        mPresenter = presenter
    }

    override fun showBills(bill: List<Bill>) {
        mAdapter?.mBills = bill.toMutableList()
        mAdapter?.notifyDataSetChanged()

        if (bill.isNotEmpty()) {
            tips?.visibility = GONE
        }
    }

    override fun deleteBill(bill: Bill) {
        mBillsChangedListener?.onBillDeleted(bill)

        val index = mAdapter?.mBills!!.indexOf(bill)
        if (index >= 0) {
            mAdapter?.mBills!!.removeAt(index)
            mAdapter?.notifyItemRemoved(index)
        }

        if (mAdapter?.mBills!!.isEmpty()) {
            showNoBills()
        }
    }

    override fun updatedBill(bill: Bill) {
        mBillsChangedListener?.onBillUpdated(bill)

        val index = mAdapter?.mBills?.indexOf(bill) ?: -1
        if (index >= 0) {
            mAdapter?.mBills?.set(index, bill)
            mAdapter?.notifyItemChanged(index)
        }
    }

    override fun addedBill(bill: Bill) {
        mBillsChangedListener?.onBillAdded(bill)

        mAdapter?.mBills?.let {
            it.add(0, bill)
            mAdapter?.notifyItemInserted(0)
            mAdapter?.notifyItemRangeChanged(0, 2)
            details.scrollToPosition(0)
        }

        tips?.visibility = GONE
    }

    override fun showLoadingBillsError() {

    }

    override fun setLoadingIndicator(boolean: Boolean) {

    }

    override fun showNoBills() {
        if (tips == null) {
            noDataStub.inflate()
        }
        tips.visibility = VISIBLE
        mAdapter?.mBills = ArrayList()
    }

    fun setOnBillsChangedListener(l: OnBillsChangedListener) {
        mBillsChangedListener = l
    }

    fun updateDate(year: Int, month: Int) {
        mPresenter?.loadBillsByYearAndMonth(year, month)
    }

    interface OnBillsChangedListener {
        fun onBillDeleted(bill: Bill)
        fun onBillUpdated(bill: Bill)
        fun onBillAdded(bill: Bill)
    }
}