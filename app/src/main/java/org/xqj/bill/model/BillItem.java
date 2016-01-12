package org.xqj.bill.model;

import io.realm.RealmObject;

/**
 * 账单项,对应一条收入/支出记录
 *
 * @author Chaos
 *         2016/01/10.
 */
public class BillItem extends RealmObject {

    private boolean income;// true 为收入

    private ConsumptionType consumptionType;// 消费类型

    private float sum;// 金额

    private long dateTime;// 隶属于哪天

    private String note;// 备注

    public ConsumptionType getConsumptionType() {
        return this.consumptionType;
    }

    public void setConsumptionType(ConsumptionType consumptionType) {
        this.consumptionType = consumptionType;
    }

    public float getSum() {
        return this.sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isIncome() {
        return income;
    }

    public void setIncome(boolean income) {
        this.income = income;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}