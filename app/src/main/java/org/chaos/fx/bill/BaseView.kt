package org.chaos.fx.bill

/**
 * @author Chaos
 *         18/05/2017
 */
interface BaseView<in T : BasePresenter> {

    fun setPresenter(presenter: T)
}