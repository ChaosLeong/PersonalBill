package org.chaos.fx.bill

import android.app.Application
import org.chaos.fx.bill.PreferenceHelper

/**
 * @author Chaos
 *         18/05/2017
 */

class BillApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.initialize(this)
    }
}