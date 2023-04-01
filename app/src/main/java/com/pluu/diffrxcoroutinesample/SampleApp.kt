package com.pluu.diffrxcoroutinesample

import android.app.Application
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import logcat.logcat

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)

        RxJavaPlugins.setErrorHandler {
            logcat { "RxJavaPlugins[Thread=${Thread.currentThread().name}] ${it.message}" }
        }
    }
}