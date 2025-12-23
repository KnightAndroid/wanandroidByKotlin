package com.core.library_base.vm

import com.core.library_base.contact.EmptyContract


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/23 14:51
 * @descript:空vm
 */
class EmptyMviViewModel :
    BaseMviViewModel<
            EmptyContract.Event,
            EmptyContract.State,
            EmptyContract.Effect>() {

    override fun initialState(): EmptyContract.State =
        EmptyContract.State

    override fun handleEvent(event: EmptyContract.Event) {
        // no-op
    }
}