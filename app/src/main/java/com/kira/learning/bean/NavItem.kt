package com.kira.learning.bean

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.kira.learning.R

/**
 * 侧边栏菜单项数据类
 * @property id 唯一标识符
 * @property title 菜单标题文本
 * @property iconRes 本地图标资源ID（优先级高于iconUrl）
 * @property iconUrl 网络图标URL（可选）
 * @property fragmentClass 关联的Fragment类
 * @property badgeCount 角标数量（如消息数）
 * @property isHeader 是否作为分组标题（不可点击）
 * @property requiresAuth 是否需要登录后才显示
 */
data class NavItem(
    val id: Int,
    val title: String,
    @DrawableRes val iconRes: Int = R.drawable.image_face_failed,
    val iconUrl: String? = "",
    val fragmentClass: Class<out Fragment>? = null,
    var badgeCount: Int = 0,
    val isHeader: Boolean = false,
    val requiresAuth: Boolean = false
) : Parcelable {

    // 兼容Parcelable（用于Activity/Fragment间传递）
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString(),
        Class.forName(parcel.readString()!!) as? Class<out Fragment>,
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeInt(iconRes)
        parcel.writeString(iconUrl)
        parcel.writeString(fragmentClass?.name)
        parcel.writeInt(badgeCount)
        parcel.writeByte(if (isHeader) 1 else 0)
        parcel.writeByte(if (requiresAuth) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NavItem> {
        override fun createFromParcel(parcel: Parcel): NavItem = NavItem(parcel)
        override fun newArray(size: Int): Array<NavItem?> = arrayOfNulls(size)
    }

    // 扩展函数：创建Fragment实例
    fun newInstance(args: Bundle? = null): Fragment? {
        return fragmentClass?.run {
            try {
                newInstance().apply {
                    args?.let { arguments = it }
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}