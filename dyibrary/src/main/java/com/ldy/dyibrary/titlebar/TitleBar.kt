package com.ldy.dyibrary.titlebar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ldy.dyibrary.R
import com.ldy.dyibrary.titlebar.data.TitleItem
import com.ldy.dyibrary.util.PixelUtils

/**
 * 标题栏
 */
class TitleBar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var mTvTitle: TextView? = null//标题
    private var mLayoutNavigation: LinearLayout? = null//导航栏
    private var mLayoutMenu: LinearLayout? = null//菜单栏

    private var titleSize: Int = 0//标题大小
    private var titleColor: Int = 0//标题颜色
    private var navigationTextSize: Int = 0//导航栏文字大小
    private var navigationTextColor: Int = 0//导航栏文字颜色
    private var menuTextSize: Int = 0//菜单栏文字大小
    private var menuTextColor: Int = 0//菜单栏文字颜色

    init {
        //初始化
        initView(context, attrs)
        //初始化标题
        initTitle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var mode = MeasureSpec.getMode(heightMeasureSpec)
        if (mode != MeasureSpec.EXACTLY) {
            var height = PixelUtils.dp2px(context, 48f)
            var newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * 初始化
     */
    private fun initView(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            var typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
            typedArray.apply {
                titleSize = getInteger(R.styleable.TitleBar_title_size, 18)
                titleColor = getColor(R.styleable.TitleBar_title_color, Color.BLACK)
                navigationTextSize = getInteger(R.styleable.TitleBar_navigation_text_size, 14)
                navigationTextColor = getColor(R.styleable.TitleBar_navigation_text_color, Color.GRAY)
                menuTextSize = getInteger(R.styleable.TitleBar_menu_text_size, 14)
                menuTextColor = getColor(R.styleable.TitleBar_menu_text_color, Color.GRAY)
            }
            typedArray.recycle()
        }
        View.inflate(context, R.layout.layout_title_bar, this@TitleBar)
        mTvTitle = findViewById(R.id.tv_title)
        mLayoutNavigation = findViewById(R.id.layout_navigation)
        mLayoutMenu = findViewById(R.id.layout_menu)
    }

    /**
     * 初始化标题
     */
    private fun initTitle() {
        mTvTitle?.textSize = titleSize.toFloat()
        mTvTitle?.setTextColor(titleColor)
    }

    /**
     * 设置标题
     */
    fun setTitle(@StringRes resId: Int) {
        mTvTitle?.text = context.getString(resId)
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        mTvTitle?.text = title
    }

    /**
     * 添加导航栏项
     */
    fun addNavigations(navigationList: List<TitleItem>?) {
        navigationList?.run {
            mLayoutNavigation?.removeAllViews()
            for (item in navigationList) {
                addNavigation(item)
            }
        }
    }

    /**
     * 添加导航栏项
     */
    fun addNavigation(item: TitleItem?) {
        item?.run {
            getTitleItem(item, true)?.let {
                mLayoutNavigation?.addView(it)
            }
        }
    }

    /**
     * 添加菜单栏项
     */
    fun addMenus(menuList: List<TitleItem>?) {
        menuList?.run {
            mLayoutMenu?.removeAllViews()
            for (item in menuList) {
                addMenu(item)
            }
        }
    }

    /**
     * 添加菜单栏项
     */
    fun addMenu(item: TitleItem?) {
        item?.run {
            getTitleItem(this, false)?.let {
                mLayoutMenu?.addView(it)
            }
        }
    }

    /**
     * 加载 TitleItem
     */
    fun getTitleItem(item: TitleItem?, isNavigation: Boolean): View? {
        var view: View? = null
        item?.run {
            view = when {
                src > 0 -> createImage(src)
                else -> createText(content, background, isNavigation)
            }.apply {
                id = itemId
            }
        }
        return view
    }

    /**
     * 创建Image
     */
    private fun createImage(src: Int): ImageView {
        var imageSize = PixelUtils.dp2px(context, 36f)
        var padding = PixelUtils.dp2px(context, 8f)
        return ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(imageSize, imageSize)
            setPadding(padding, padding, padding, padding)
            setImageResource(src)
        }
    }

    /**
     * 创建Text
     */
    private fun createText(text: String?, background: Int, isNavigation: Boolean): TextView {
        return TextView(context).apply {
            if (background > 0) {
                val paddingH = PixelUtils.dp2px(context, 8f)
                val paddingV = PixelUtils.dp2px(context, 3f)
                val margin = PixelUtils.dp2px(context, 8f)
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    setMargins(margin, margin, margin, margin)
                }
                gravity = Gravity.CENTER
                setPadding(paddingH, paddingV, paddingH, paddingV)
                setBackgroundResource(background)
            } else {
                val paddingH = PixelUtils.dp2px(context, if (isNavigation) 6f else 12f)
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
                setPadding(paddingH, 0, paddingH, 0)
            }
            setText(text)
            setTextColor(if (isNavigation) navigationTextColor else menuTextColor)
            textSize = if (isNavigation) navigationTextSize.toFloat() else menuTextSize.toFloat()
        }
    }

    /**
     * 设置导航栏的点击事件
     */
    fun setNavigationClickListener(navigationClickListener: OnClickListener?) {
        for (i in 0..mLayoutNavigation!!.childCount) {
            mLayoutNavigation!!.getChildAt(i)?.setOnClickListener(navigationClickListener)
        }
    }

    /**
     * 设置菜单栏的点击事件
     */
    fun setMenuClickListener(menuClickListener: OnClickListener?) {
        for (i in 0..mLayoutMenu!!.childCount) {
            mLayoutMenu!!.getChildAt(i)?.setOnClickListener(menuClickListener)
        }
    }

}