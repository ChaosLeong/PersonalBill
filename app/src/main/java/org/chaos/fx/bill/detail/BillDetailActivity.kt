package org.chaos.fx.bill.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import org.chaos.fx.bill.R
import org.chaos.fx.bill.data.BillsRepository

/**
 * @author Chaos
 *         19/05/2017
 */
class BillDetailActivity : AppCompatActivity() {

    companion object {
        val KEY_BILL_ID = "bill_id"

        fun startForResult(act: Activity, requestCode: Int, id: Long?) {
            val starter = Intent(act, BillDetailActivity::class.java)
            id?.let {
                starter.putExtra(KEY_BILL_ID, it)
            }
            act.startActivityForResult(starter, requestCode)
        }

        fun startForResult(fragment: Fragment, requestCode: Int, id: Long?) {
            val starter = Intent(fragment.activity, BillDetailActivity::class.java)
            id?.let {
                starter.putExtra(KEY_BILL_ID, it)
            }
            fragment.startActivityForResult(starter, requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id: Long?
        if (intent.hasExtra(KEY_BILL_ID)) {
            id = intent.getLongExtra(KEY_BILL_ID, 0)
        } else {
            id = null
        }
        val fragment = supportFragmentManager.findFragmentById(R.id.container) as BillDetailFragment? ?: BillDetailFragment()
        fragment.setPresenter(BillDetailPresenter(id, fragment, BillsRepository(this)))

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}