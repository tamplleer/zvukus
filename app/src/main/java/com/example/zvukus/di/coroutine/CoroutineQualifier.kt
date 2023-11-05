package com.example.zvukus.di.coroutine
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val zvukusDispatcher: ZvukusDispatchers)

enum class ZvukusDispatchers {
    IO,
    Main,
    Default,
}