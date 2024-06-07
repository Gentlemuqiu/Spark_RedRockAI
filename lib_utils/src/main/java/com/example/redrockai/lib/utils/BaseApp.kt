package com.example.redrockai.lib.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


open class BaseApp : Application() {


    companion object {
        var instance: BaseApp? = null
        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
        // 创建一个全局的协程作用域
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initARouter()
    }

    /**
     * 在单模块调试时也需要该 ARouter，所以直接在这里初始化
     */
    private fun initARouter() {
        val isDebugARouter = true
        if (isDebugARouter) {
            //下面两行必须写在init之前，否则这些配置在init中将无效
            ARouter.openLog();
            //开启调试模式（如果在InstantRun模式下运行，必须开启调试模式！
            // 线上版本需要关闭，否则有安全风险）
            ARouter.openDebug();
        }
        ARouter.init(this)
        Log.d("lxh", "initARouter: 加载成功")
    }


}