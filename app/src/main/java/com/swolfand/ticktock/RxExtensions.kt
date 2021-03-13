package com.swolfand.ticktock

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * An extension function on [CompositeDisposable] that makes the following possible:
 * compositeDisposable += observable.subscribe()
 */
operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}
