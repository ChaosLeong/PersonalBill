package org.chaos.fx.bill.data

import io.reactivex.Observable

/**
 * @author Chaos
 *         18/05/2017
 */
interface BillsDataSource {
    fun getBills(): Observable<List<Bill>>
    fun getBillsByTimes(greater: Long, lesser: Long): Observable<List<Bill>>
    fun getBill(id: Long): Observable<Bill>
    fun getBillWithRefreshCache(id: Long): Observable<Bill>
    fun saveBill(bill: Bill): Long
    fun deleteBill(id: Long)
}