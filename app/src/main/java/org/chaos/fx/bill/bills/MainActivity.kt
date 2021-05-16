package org.chaos.fx.bill.bills

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_main_appbar.*
import org.chaos.fx.bill.R
import org.chaos.fx.bill.data.BillsRepository
import java.util.*


class MainActivity : AppCompatActivity(), MainContract.View {

    private val RELATIVE_SIZE = 1.3f

    private var mPresenter: MainContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val repository = BillsRepository(this)
        val mainPresenter = MainPresenter(this, repository)
        setPresenter(mainPresenter)
        mPresenter?.subscribe()

        val fragment: BillsFragment = supportFragmentManager.findFragmentById(R.id.container) as BillsFragment? ?: BillsFragment()
        val presenter = BillsPresenter(fragment, repository)
        fragment.setPresenter(presenter)
        fragment.setOnBillsChangedListener(mainPresenter)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.unsubscribe()
    }

    override fun setPresenter(presenter: MainContract.Presenter) {
        mPresenter = presenter
    }

    override fun updateHeaderDate(year: Int, month: Int) {
        val cal = Calendar.getInstance()
        cal.clear()
        cal.set(year, month, 1)
        DateFormat.format(getString(R.string.header_year_month_format), cal)

        val dateString = DateFormat.format(getString(R.string.header_year_month_format), cal)
        updateHeaderText(dateText, dateString)
    }

    override fun updateHeaderIncome(income: Float) {
        val incomeString = String.format(getString(R.string.header_income_format), income)
        updateHeaderText(incomeText, incomeString)
    }

    override fun updateHeaderExpense(expense: Float) {
        val expenseString = String.format(getString(R.string.header_expense_format), expense)
        updateHeaderText(expenseText, expenseString)
    }

    private fun updateHeaderText(tv: TextView, text: CharSequence) {
        val start = text.indexOf("\n") + 1
        val end = text.length
        val builder = SpannableStringBuilder(text)
        builder.setSpan(RelativeSizeSpan(RELATIVE_SIZE), start, end, 0)
        builder.setSpan(
                ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.primary_text, theme)),
                start, end, 0)
        tv.text = builder
    }
}
