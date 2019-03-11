package com.ldy.dyibrary.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.ViewConfiguration
import java.lang.reflect.Field

class PhoneBarUtils {

    companion object {

        /**
         * 获取手机状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            var c: Class<*>? = null
            var obj: Any? = null
            var field: Field? = null
            var x = 0
            var statusBarHeight = 0
            try {
                c = Class.forName("com.android.internal.R\$dimen")
                obj = c!!.newInstance()
                field = c.getField("status_bar_height")
                x = Integer.parseInt(field!!.get(obj).toString())
                statusBarHeight = context.resources.getDimensionPixelSize(x)
            } catch (e1: Exception) {
                e1.printStackTrace()
            }

            return statusBarHeight
        }

        /**
         * 获取虚拟按键的高度
         */
        fun getNavigationBarHeight(context: Context): Int {
            var result = 0
            if (hasNavBar(context)) {
                val res = context.resources
                val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    result = res.getDimensionPixelSize(resourceId)
                }
            }
            return result
        }

        /**
         * 检查是否存在虚拟按键栏
         */
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        private fun hasNavBar(context: Context): Boolean {
            val res = context.resources
            val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
            return if (resourceId != 0) {
                var hasNav = res.getBoolean(resourceId)
                // check override flag
                val sNavBarOverride = getNavBarOverride()
                if ("1" == sNavBarOverride) {
                    hasNav = false
                } else if ("0" == sNavBarOverride) {
                    hasNav = true
                }
                hasNav
            } else { // fallback
                !ViewConfiguration.get(context).hasPermanentMenuKey()
            }
        }

        /**
         * 判断虚拟按键栏是否重写
         */
        private fun getNavBarOverride(): String? {
            var sNavBarOverride: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m = c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (e: Throwable) {
                }

            }
            return sNavBarOverride
        }
    }
}