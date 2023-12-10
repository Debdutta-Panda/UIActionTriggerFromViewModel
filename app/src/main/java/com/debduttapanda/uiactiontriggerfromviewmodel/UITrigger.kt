package com.debduttapanda.uiactiontriggerfromviewmodel

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext

class Event<T>(val value: T){
    var consumed: Boolean = false
        private set
    val consume: T?
        get() {
            if(!consumed){
                consumed = true
                return value
            }
            return null
        }
}
@Composable
fun HandleTrigger(
    eventState: State<Event<UIEvent>?>,
    fulfillment: (context: Context, data: UIEvent)->Unit
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = eventState.value){
        if(eventState.value?.consumed == false){
            val event = eventState.value?.consume
            if(event!=null){
                fulfillment(context, event)
            }
            event?.trigger?.onDone()
        }
    }
}

open class UIEvent{
    internal var trigger: UITrigger? = null
}



class UITrigger{
    private val queue = mutableListOf<UIEvent>()
    var busy: Boolean = false
        private set
    private var lastStarted = 0L
    private val _state = mutableStateOf<Event<UIEvent>?>(null)
    fun post(event: UIEvent){
        val now = System.currentTimeMillis()
        if(busy && now - lastStarted < 1000){
            queue.add(event)
            return
        }
        busy = true
        lastStarted = now
        event.trigger = this
        _state.value = Event(event)
    }

    fun onDone() {
        busy = false
        if(queue.isNotEmpty()){
            post(queue.removeFirst())
        }
    }

    val state: State<Event<UIEvent>?>
        get() = _state
}