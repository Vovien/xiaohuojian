package com.jmbon.middleware.kotlinbus


import android.util.Log
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 *
 * @FileName:
 *          com.safframework.eventbus.EventBus
 * @author: Tony Shen
 * @date: 2019-08-24 23:28
 * @version: V1.0 <描述当前版本功能>
 */
val UI: CoroutineDispatcher = Dispatchers.Main

object KotlinBus : CoroutineScope {

    private val TAG = "KotlinBus"

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.Default + job

    private val contextMap = ConcurrentHashMap<String, MutableMap<Class<*>, EventData<*>>>()
    private val mStickyEventMap = ConcurrentHashMap<Class<*>, Any>()

    @JvmStatic
    fun <T> register(
        contextName: String,
        eventDispatcher: CoroutineDispatcher = UI,
        eventClass: Class<T>,
        eventCallback: (T) -> Unit
    ) {
        Log.i(TAG, "register:$contextName")
        val eventDataMap = if (contextMap.containsKey(contextName)) {
            contextMap[contextName]!!
        } else {
            val eventDataMap = mutableMapOf<Class<*>, EventData<*>>()
            contextMap[contextName] = eventDataMap
            eventDataMap
        }

        eventDataMap[eventClass] = EventData(this, eventDispatcher, eventCallback)
    }

    @JvmStatic
    fun <T> register(
        contextName: String,
        eventDispatcher: CoroutineDispatcher = UI,
        eventClass: Class<T>,
        eventCallback: (T) -> Unit,
        eventFail: (Throwable) -> Unit
    ) {
        val eventDataMap = if (contextMap.containsKey(contextName)) {
            contextMap[contextName]!!
        } else {
            val eventDataMap = mutableMapOf<Class<*>, EventData<*>>()
            contextMap[contextName] = eventDataMap
            eventDataMap
        }

        eventDataMap[eventClass] = EventData(this, eventDispatcher, eventCallback, eventFail)
    }

    @JvmStatic
    fun <T> registerSticky(
        contextName: String,
        eventDispatcher: CoroutineDispatcher = UI,
        eventClass: Class<T>,
        eventCallback: (T) -> Unit
    ) {
        val eventDataMap = if (contextMap.containsKey(contextName)) {
            contextMap[contextName]!!
        } else {
            val eventDataMap = mutableMapOf<Class<*>, EventData<*>>()
            contextMap[contextName] = eventDataMap
            eventDataMap
        }

        eventDataMap[eventClass] = EventData(this, eventDispatcher, eventCallback)

        val event = mStickyEventMap[eventClass]
        event?.let {

            postEvent(it)
        }
    }

    @JvmStatic
    fun <T> registerSticky(
        contextName: String,
        eventDispatcher: CoroutineDispatcher = UI,
        eventClass: Class<T>,
        eventCallback: (T) -> Unit,
        eventFail: (Throwable) -> Unit
    ) {
        val eventDataMap = if (contextMap.containsKey(contextName)) {
            contextMap[contextName]!!
        } else {
            val eventDataMap = mutableMapOf<Class<*>, EventData<*>>()
            contextMap[contextName] = eventDataMap
            eventDataMap
        }

        eventDataMap[eventClass] = EventData(this, eventDispatcher, eventCallback, eventFail)

        val event = mStickyEventMap[eventClass]
        event?.let {

            postEvent(it)
        }
    }

    @JvmStatic
    fun post(event: Any, delayTime: Long = 0) {

        if (delayTime > 0) {
            launch {
                delay(delayTime)
                postEvent(event)
            }
        } else {
            postEvent(event)
        }
    }

    @JvmStatic
    fun postSticky(event: Any) {
        mStickyEventMap[event.javaClass] = event
    }

    @JvmStatic
    fun unregisterAllEvents() {

        Log.i(TAG, "unregisterAllEvents()")

        coroutineContext.cancelChildren()
        for ((_, eventDataMap) in contextMap) {
            eventDataMap.values.forEach {
                it.cancel()
            }
            eventDataMap.clear()
        }
        contextMap.clear()
    }

    @JvmStatic
    fun unregister(contextName: String) {

        Log.i(TAG, "unregister:$contextName")
        val cloneContexMap = ConcurrentHashMap<String, MutableMap<Class<*>, EventData<*>>>()
        cloneContexMap.putAll(contextMap)
        val map = cloneContexMap.filter { it.key == contextName }
        for ((_, eventDataMap) in map) {
            eventDataMap.values.forEach {
                it.cancel()
            }
            eventDataMap.clear()
        }
        contextMap.remove(contextName)
    }

    @JvmStatic
    fun <T> removeStickyEvent(eventType: Class<T>) {
        mStickyEventMap.remove(eventType)
    }

    private fun postEvent(event: Any) {
        val cloneContexMap = ConcurrentHashMap<String, MutableMap<Class<*>, EventData<*>>>()
        cloneContexMap.putAll(contextMap)
        for ((_, eventDataMap) in cloneContexMap) {
            eventDataMap.keys
                .firstOrNull { it == event.javaClass || it == event.javaClass.superclass }
                ?.let { key ->
                    eventDataMap[key]?.postEvent(event)
                    //Log.e("TAG SENDEvent", "${event.toString()}")
                }
        }
    }
}