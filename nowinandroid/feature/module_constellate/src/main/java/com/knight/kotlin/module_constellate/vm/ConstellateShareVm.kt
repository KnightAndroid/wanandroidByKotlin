package com.knight.kotlin.module_constellate.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


/**
 * @author created by luguian
 * @organize
 * @Date 2025/9/12 15:15
 * @descript:共享vm
 */
class ConstellateShareVm : ViewModel() {

    private val _selectedConstellate = MutableSharedFlow<ConstellateTypeEntity>(extraBufferCapacity = 1)
    val selectedConstellate = _selectedConstellate.asSharedFlow()

    fun selectConstellate(entity: ConstellateTypeEntity) {
        viewModelScope.launch {
            _selectedConstellate.emit(entity)
        }
    }
}