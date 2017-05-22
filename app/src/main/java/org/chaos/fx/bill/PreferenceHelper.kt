package org.chaos.fx.bill

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * @author Chaos
 *         18/05/2017
 */

object PreferenceHelper {

    private var mPreference: SharedPreferences? = null

    fun initialize(context: Context) {
        mPreference = PreferenceManager.getDefaultSharedPreferences(context)
    }
}