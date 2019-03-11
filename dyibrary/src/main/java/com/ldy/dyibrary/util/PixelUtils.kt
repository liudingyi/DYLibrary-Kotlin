package com.ldy.dyibrary.util

import android.content.Context
import android.util.TypedValue

/**
 * 像素工具
 */
class PixelUtils {

    companion object {

        /**
         * sp转成px
         */
        fun sp2px(context: Context, size: Double): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size.toFloat(), context.resources.displayMetrics)
        }

        /**
         * dp转成px
         */
        fun dp2px(context: Context, size: Float): Int {
            return dp2pxFloat(context, size).toInt()
        }

        /**
         * dp转成px
         */
        fun dp2pxFloat(context: Context, size: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics)
        }

        /**
         * 获取屏幕宽度
         */
        fun getScreenWidth(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }

        /**
         * 获取屏幕高度
         */
        fun getScreenHeight(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }

    }

}