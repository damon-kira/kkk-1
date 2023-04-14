package com.bigdata.lib.bean

class AppInfo {
    var pkgname: String? = null //包名
    var appname: String? = null//名称
    var installtime: String? = null//按照时间
    var installtime_utc: String? = null //
    var timestamps: String? = null//
    var last_timestamps: String? = null//最后更新时间
    var type: Int = -1 // 是否是预装 0:预装 1:非预装
    var appInfoVersion: String? = null// app版本
    var devicesId:String? = null //设备id
}