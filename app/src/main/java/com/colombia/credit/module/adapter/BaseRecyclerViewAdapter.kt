package com.colombia.credit.module.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.util.lib.ifShow
import org.jetbrains.annotations.NotNull

/**
 * Created by weisl on 2018/9/12.
 */
abstract class BaseRecyclerViewAdapter<T>(
    @NotNull private val items: ArrayList<T>,
    @LayoutRes protected var layoutRes: Int
) : RecyclerView.Adapter<BaseViewHolder>() {

    protected var mListener: OnItemClickListener<T>? = null
    protected var currentItems = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
    }

    override fun getItemCount(): Int = currentItems.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, currentItems[position], position)
    }

    abstract fun convert(holder: BaseViewHolder, item: T, position: Int)

    /**
     * 添加更多数据
     */
    fun addItems(t: ArrayList<T>?) {
        t?.isEmpty() ?: return
        items.addAll(t)
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setItems(t: List<T>?) {
        items.clear()
        if (t != null)
            items.addAll(t)
        currentItems = items
        notifyDataSetChanged()
    }

    fun <T> getItemData(position: Int): T? {
        if (position < 0) return null
        return currentItems[position] as T
    }

    interface OnItemClickListener<T> {
        fun onItemClick(item: T?, position: Int)
    }

    open fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        this.mListener = listener
    }

    protected fun getColor(context: Context, @ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }
}

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val sparseArray = SparseArray<View>()

    fun getContext(): Context {
        return itemView.context
    }

    private fun <V: View> findViewById(@IdRes id: Int): V {
        var view = sparseArray.get(id)
        if (view == null) {
            view = itemView.findViewById<V>(id)
            sparseArray.put(id, view)
        }
        return view as V
    }

    fun setText(@IdRes viewId: Int, text: String?) {
        findViewById<TextView>(viewId).text = text
    }

    fun setText(@IdRes viewId: Int, @StringRes strRes: Int) {
        findViewById<TextView>(viewId).setText(strRes)
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes drawableRes: Int) {
        findViewById<ImageView>(viewId).setImageResource(drawableRes)
    }

    fun <T : View> getView(@IdRes viewId: Int): T {
        return findViewById(viewId)
    }

    fun <T : View> getView(rootView: View, @IdRes viewId: Int): T {
        return rootView.findViewById(viewId)
    }

    fun setTextColor(@IdRes viewId: Int, @ColorRes colorRes: Int) {
        findViewById<TextView>(viewId)
            .setTextColor(getContext().resources.getColor(colorRes))
    }

    /**
     * @param isVisiable true: 显示 false GONE
     */
    fun setVisibility(@IdRes viewId: Int, isVisiable: Boolean) {
        findViewById<View>(viewId).ifShow(isVisiable)
    }

    fun clear() {
        sparseArray.clear()
    }

}