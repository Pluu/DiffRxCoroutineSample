package com.pluu.diffrxcoroutinesample.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.diffrxcoroutinesample.data.DataSource
import com.pluu.diffrxcoroutinesample.data.GitHubService
import com.pluu.diffrxcoroutinesample.data.model.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import logcat.logcat

class MainViewModel : ViewModel() {
    private val api: GitHubService = DataSource.service
    private val autoDisposable = CompositeDisposable()

    private val _showErrorBundle = MutableLiveData<ErrorBundle>()
    val showErrorBundle: LiveData<ErrorBundle> get() = _showErrorBundle

    private val _userEvent = MutableLiveData<User>()
    val userEvent: LiveData<User> get() = _userEvent

    fun tryRxNetworkError() {
        api.tryNetworkError()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success ->
                    logcat { "Success: $success" }
                },
                { t ->
                    t.printError()
                    _showErrorBundle.value = ErrorBundle(t)
                }
            )
            .addTo(autoDisposable)
    }

    fun tryRxViewModelError() {
        api.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success ->
                    logcat { "Success: $success" }
                    throw IllegalStateException("Force exception on ViewModel")
                },
                { t ->
                    t.printError()
                    _showErrorBundle.value = ErrorBundle(t)
                }
            )
            .addTo(autoDisposable)
    }

    fun tryRxLiveDataError() {
        api.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { success ->
                    logcat { "Success: $success" }
                    _userEvent.value = success
                },
                { t ->
                    t.printError()
                    _showErrorBundle.value = ErrorBundle(t)
                }
            )
            .addTo(autoDisposable)
    }

    fun tryCoroutineNetworkError() {
        val ceh = CoroutineExceptionHandler { _, t ->
            t.printError()
            _showErrorBundle.value = ErrorBundle(t)
        }
        viewModelScope.launch(ceh) {
            val success = api.suspendTryNetworkError()
            logcat { "Success: $success" }
        }
    }

    fun tryCoroutineViewModelError() {
        val ceh = CoroutineExceptionHandler { _, t ->
            t.printError()
            _showErrorBundle.value = ErrorBundle(t)
        }
        viewModelScope.launch(ceh) {
            val success = api.suspendGetUser()
            logcat { "Success: $success" }
            throw IllegalStateException("Force exception on ViewModel")
        }
    }

    fun tryCoroutineLiveDataError() {
        val ceh = CoroutineExceptionHandler { _, t ->
            t.printError()
            _showErrorBundle.value = ErrorBundle(t)
        }
        viewModelScope.launch(ceh) {
            val success = api.suspendGetUser()
            logcat { "Success: $success" }
            _userEvent.value = success
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoDisposable.clear()
    }
}

private fun Disposable.addTo(autoDisposable: CompositeDisposable) {
    autoDisposable.add(this)
}

data class ErrorBundle(
    val throwable: Throwable
)

private fun Throwable.printError() {
    logcat { "Error [Thread=${Thread.currentThread().name}]: $message" }
}