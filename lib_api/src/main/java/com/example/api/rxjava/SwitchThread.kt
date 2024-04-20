package com.example.api.rxjava

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 *  author : lytMoon
 *  date: 2024/4/19 12:55
 *  version : 1.0
 *  description : 优化异步、回调操作
 *  saying : 这世界天才那么多，也不缺我一个
 */
object SwitchThread {

    /**
     * 从io线程切到主线程
     *
     * 传入的第一个参数是在IO线程执行的
     * 使用方式
     *    ioToMainThread<DataClass>({ ocr.ocrResult }) {
     *             //在这里传入更新UI的回调
     *             Log.d("yolo", "测试数据${it}")
     *         }
     *
     * 传入的第二个参数是可以更新UI的
     */
    @SuppressLint("CheckResult")
    inline fun <T> ioToMainThread(
        crossinline observable: () -> T,
        crossinline observer: (T) -> Unit
    ) {
        Observable
            .create {
                val data = observable()
                it.onNext(data)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                observer.invoke(it)
            }
    }


}