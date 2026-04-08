package com.core.library_base.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * @author created by luguian
 * @organize
 * @Date 2025/12/22 9:30
 * @descript:基类BaseViewModel
 */
/**
 * 用户事件
 */
interface ViewEvent

/**
 * 页面状态
 *
 */
interface ViewState

/**
 *
 * 页面要立刻干什么事
 */
interface ViewSideEffect

abstract class BaseMviViewModel<
        Event : ViewEvent,
        State : ViewState,
        Effect : ViewSideEffect
        > : ViewModel() {

    /** ========== State ========== */

    protected abstract fun initialState(): State

    private val _viewState: MutableStateFlow<State> by lazy {
        MutableStateFlow(initialState())
    }
    val viewState: StateFlow<State> = _viewState.asStateFlow()


    // ✅ 加这里
    protected val currentState: State
        get() = _viewState.value
    /** ========== Event ========== */

    private val _event = MutableSharedFlow<Event>()

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected abstract fun handleEvent(event: Event)

    init {
        viewModelScope.launch {
            _event.collect { event ->
                handleEvent(event)
            }
        }
    }

    /** ========== Effect ========== */

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }

    /** ========== State Update ========== */

    protected fun setState(reducer: State.() -> State) {
        _viewState.update { it.reducer() }
    }

    /** ========== Request (Suspend) ========== */

    protected fun <T> requestMvi(
        block: suspend () -> T,
        onStart: (() -> Unit)? = null,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            runCatching {
                onStart?.invoke()
                block()
            }.onSuccess {
                onSuccess(it)
            }.onFailure {
                onError(it)
            }
        }
    }

    /** ========== Request (Flow) ========== */

    protected fun <T> requestFlowMvi(
        block: () -> Flow<T>,
        onStart: (() -> Unit)? = null,
        onEach: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onCompletion: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            block()
                .onStart { onStart?.invoke() }
                .onEach { onEach(it) }
                .catch { onError(it) }
                .onCompletion { onCompletion?.invoke() }
                .collect()
        }
    }
}