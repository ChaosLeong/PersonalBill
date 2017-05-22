package org.chaos.fx.bill.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_add_bill.*
import org.chaos.fx.bill.R
import java.util.*

/**
 * @author Chaos
 *         19/05/2017
 */
class BillDetailFragment : Fragment(), DetailContract.View {

    private val TAG = "BillDetailFragment"

    private var mPresenter: DetailContract.Presenter? = null
    private var mTimeStamp: Long = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.activity_add_bill, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.default_categories))
        categories.adapter = adapter
        fab.setOnClickListener {
            val id = mPresenter?.saveBill(incomeButton.isChecked,
                    resources.getStringArray(R.array.default_categories)[categories.selectedItemPosition],
                    priceText.text.toString().toFloat(),
                    mTimeStamp,
                    noteText.text.toString())
            val data = Intent()
            data.putExtra(BillDetailActivity.KEY_BILL_ID, id)
            activity.setResult(Activity.RESULT_OK, data)
            activity.finish()
        }
        dateText.setOnClickListener {
            val dialog = DatePickerDialog()
            dialog.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.clear()
                cal.set(year, monthOfYear, dayOfMonth)
                showDate(cal.timeInMillis)
            }
        }
        showDate(System.currentTimeMillis())

        mPresenter?.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.unsubscribe()
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun showBillType(isIncome: Boolean) {
        if (isIncome) {
            incomeButton.isChecked = true
        } else {
            expenseButton.isChecked = true
        }
    }

    override fun showCategory(category: String) {
        val index = resources.getStringArray(R.array.default_categories).indexOf(category)
        categories.setSelection(index)
    }

    override fun showPrice(price: Float) {
        priceText.setText(price.toString())
    }

    override fun showDate(timestamp: Long) {
        mTimeStamp = timestamp
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        dateText.text = DateFormat.format(getString(R.string.detail_date_format), cal)
    }

    override fun showNote(note: String) {
        noteText.setText(note)
    }
}