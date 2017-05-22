package org.chaos.fx.bill.data

import android.content.Context
import android.util.Log
import io.reactivex.Observable

/**
 * @author Chaos
 *         18/05/2017
 */
class BillsRepository(context: Context) : BillsDataSource {

    private val mDaoSession: DaoSession
    private val mBillDao: BillDao

    init {
        val daoSession = DaoMaster(DaoMaster.DevOpenHelper(context, "bills.db").writableDb).newSession()
        mBillDao = daoSession.billDao
        mDaoSession = daoSession
    }

    override fun getBills(): Observable<List<Bill>> {
        return Observable.just(mBillDao.loadAll())
    }

    override fun getBillsByTimes(greater: Long, lesser: Long): Observable<List<Bill>> {
        return Observable.just(mBillDao.queryBuilder()
                .where(BillDao.Properties.Timestamp.lt(lesser), BillDao.Properties.Timestamp.ge(greater))
                .orderDesc(BillDao.Properties.Timestamp)
                .list())
    }

    override fun getBill(id: Long): Observable<Bill> {
        val bill = mBillDao.load(id)
        return Observable.just(bill)
    }

    override fun getBillWithRefreshCache(id: Long): Observable<Bill> {
        val bill = mBillDao.load(id)
        mBillDao.detach(bill)
        return Observable.just(mBillDao.load(id))
    }

    override fun saveBill(bill: Bill): Long {
        if (mBillDao.hasKey(bill)) {
            mBillDao.update(bill)
            return bill.id
        } else {
            val idx = mBillDao.insert(bill)
            return mBillDao.loadByRowId(idx).id
        }
    }

    override fun deleteBill(id: Long) {
        mBillDao.deleteByKey(id)
    }
}