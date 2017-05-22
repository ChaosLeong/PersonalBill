package org.chaos.fx.bill

import android.content.SharedPreferences

/**
 * @author Chaos
 *         18/05/2017
 */
fun SharedPreferences.editor(f: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    f(editor)
    editor.apply()
}