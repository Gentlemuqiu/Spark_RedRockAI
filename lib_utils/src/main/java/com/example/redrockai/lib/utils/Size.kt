package com.example.redrockai.lib.utils

import android.graphics.drawable.Drawable
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email guo985892345@foxmail.com
 * @date 2022/8/4 11:28
 */

val Int.dp2pxF: Float
    get() = BaseApp.getAppContext().resources.displayMetrics.density * this

val Int.dp2px: Int
    get() = dp2pxF.toInt()

val Float.dp2pxF: Float
    get() =  BaseApp.getAppContext().resources.displayMetrics.density * this

val Float.dp2px: Int
    get() = dp2pxF.toInt()

val Int.px2dpF: Float
    get() = this /  BaseApp.getAppContext().resources.displayMetrics.density

val Int.px2dp: Int
    get() = px2dpF.toInt()

val Float.px2dpF: Float
    get() = this /  BaseApp.getAppContext().resources.displayMetrics.density

val Float.px2dp: Int
    get() = px2dpF.toInt()

val Int.dp2spF: Float
    get() =  BaseApp.getAppContext().resources.displayMetrics.scaledDensity * this

val Int.dp2sp: Float
    get() = dp2spF * this

val Float.dp2spF: Float
    get() =  BaseApp.getAppContext().resources.displayMetrics.scaledDensity * this

val Float.dp2sp: Float
    get() = dp2spF * this

val Int.sp2dpF: Float
    get() = this /  BaseApp.getAppContext().resources.displayMetrics.scaledDensity

val Int.sp2dp: Float
    get() = sp2dpF * this

val Float.sp2dpF: Float
    get() = this /  BaseApp.getAppContext().resources.displayMetrics.scaledDensity

val Float.sp2dp: Float
    get() = sp2dpF * this

val Int.color: Int
    get() = ContextCompat.getColor( BaseApp.getAppContext(), this)

val Int.string: String
    get() =  BaseApp.getAppContext().getString(this)

val Int.drawable: Drawable
    get() = AppCompatResources.getDrawable( BaseApp.getAppContext(), this)!!

val Int.dimen: Float
    get() =  BaseApp.getAppContext().resources.getDimension(this)

val Int.anim: Animation
    get() = AnimationUtils.loadAnimation( BaseApp.getAppContext(), this)

val screenWidth: Int
    get() =  BaseApp.getAppContext().resources.displayMetrics.widthPixels

val screenHeight: Int
    get() =  BaseApp.getAppContext().resources.displayMetrics.heightPixels