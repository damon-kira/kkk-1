package com.bigdata.lib.bean

import com.google.gson.annotations.SerializedName

class BaseInfo {
    @SerializedName("Z2Zu")
    var isSwitchPages: Int = 0// 	认证时，是否有切换去别的页面，只要有一次就是传 1是0否

    @SerializedName("nvlg")
    var readPrivacyAgreementTime: Long = 0// 	阅读隐私协议最长时间

    var readPrivacyAgreementTimes: Int = 0 //    阅读隐私协议次数

    @SerializedName("lhaiM8s")
    var registWifi: String? = null//    注册时wifi

    @SerializedName("KiG8E6RmN")
    var registIp: String? = null//    注册时ip

    @SerializedName("rBL3Mb")
    var faceCheckWifi: String? = null//    借款人脸时使用的wifi

    @SerializedName("m6gXmsp")
    var loanRequestWifi: String? = null//    借款时使用的wifi

    @SerializedName("mULOYA4TP")
    var internetType: String? = null //网络类型wifi4G3g

    @SerializedName("ftAHiAw9fO")
    var bootDate: String? = null//    开机日期保存时间戳精确到秒

    @SerializedName("OV1l")
    var bootTime: String? = null//    开机时长单位为秒

    @SerializedName("vQn0YMSU")
    var batteryPower: Int = 0//    电池电量0-100

    @SerializedName("JP3F9L")
    var isRoot: Int = 0//    是否root1是0否

    @SerializedName("gkisJWleS4")
    var systemVersion: String? = null//    系统版本

    @SerializedName("FWqfQ")
    var screenRateLong: Int = 0//    分辨率长

    @SerializedName("nI4I")
    var screenRateWidth: Int = 0//    分辨率宽

    //  ocr图片exif的JSON如果不存在motherfuck，thisisarealpicture存在既按{ “extractGps”:””, 拍摄时GPS”extractTakeTime”:””, 拍摄日期”extractPohotoSize”:””, 图片内存100kb”extractPohotoPixel”:””, 图片像素100x100”extractTakeDevice”:””拍摄设备 }格式保存
    @SerializedName("p4yg")
    var ocrPhotoExif: String? = null

    //   人脸识别图片exif的JSON如果不存在motherfuck，thisisarealpicture存在既按{ “extractGps”:””, 拍摄时GPS”extractTakeTime”:””, 拍摄日期”extractPohotoSize”:””, 图片内存100kb”extractPohotoPixel”:””, 图片像素100x100”extractTakeDevice”:””拍摄设备 }格式保存
    @SerializedName("Bgp3rnTyWw")
    var faceCheckExif: String? = null

    @SerializedName("ztPGv")
    var imei: String? = null//    手机序列号

    @SerializedName("IR7r")
    var imei2: String? = null//手机序列号2
    @SerializedName("lym8")
    var gaid: String? = null//广告ID
    @SerializedName("ROOz2sEJg")
    var imsi: String? = null//    国际移动用户识别码
    @SerializedName("tCd2m")
    var androidId: String? = null//    安卓ID
    @SerializedName("urwHNV")
    var mac: String? = null//    mac
    @SerializedName("uMUi2")
    var storageTotalSize: String? = null//    设备总容量
    @SerializedName("mKqdLDPj8n")
    var storageAvailableSize: String? = null//    设备可用余量
    @SerializedName("krDyE")
    var sdCardTotalSize: String? = null//    sd卡总容量
    @SerializedName("DoH4")
    var sdCardAvailableSize: String? = null//    sd卡可用余量
    @SerializedName("kcE4n95eH7")
    var usageTimeBeforeOrder: Long = 0//    使用期间
    @SerializedName("H8bxPlc")
    var firstUseAndRequestIntervalTime: Long = 0//    首次使用到申请时间间隔
    @SerializedName("QHK6TZL")
    var loginAccountEnterTime: Long = 0//    登录手机号输入时长
    @SerializedName("MWU7yFy")
    var backgroundRecoveryTimes: Int = 0//    当前使用从后台恢复次数
    @SerializedName("HE8L5")
    var chargingStatus: Int = 0//    是否充电状态0否1是
    @SerializedName("grUwT8dB")
    var simState: Int = 0//    SIM状态1无卡2单卡3双卡
    @SerializedName("MnLaA7y")
    var timeZone: String? = null//    时区
    @SerializedName("TYZP9NJr9F")
    var vpn: String? = null//    vpn
    @SerializedName("DlzQMdD6O")
    var phoneLanguage: String? = null//    手机语言
    @SerializedName("pKFtvq")
    var screenBrightness: Int = 0//    屏幕亮度
    @SerializedName("atND")
    var mcc: String? = null//    mcc
    @SerializedName("Jliv")
    var mnc: String? = null//    mnc
    @SerializedName("MRcTZipXmq")
    var developerStatus: Int = 0//    是否开启开发者状态0否1是
    @SerializedName("NxLdJ")
    var addresSimulationApp: Int = 0//    是否安装模拟软件0否1是
    @SerializedName("SZJeZXvN")
    var operators: String? = null//    手机运营商
    @SerializedName("ZfKe")
    var loanPageStayTime: Long = 0//    贷款页面停留时间
    @SerializedName("Upl3cV1kNO")
    var wifiList: String? = null//    wifi列表json格式：[{ “name”:”ddd”, ”ip”:”163.195.6.6” }]

    //GPS欺诈软件APP列表[{ “firstInstallTime”:1619170976886, ”appName”:”FakeGPS”, ”packageName”:”com.lexa.fakegps”, ”lastUpdateTime”:1619170976886 }]
    @SerializedName("KUH4u9dqy1")
    var gpsFakeAppList: String? = null
    @SerializedName("Um5UkV3Q")
    var advertisingId: String? = null// 广告id
    @SerializedName("ZDVGq3")
    var photoAlbumListUrl: String? = null//    相册图片列表url
    @SerializedName("YycK")
    var isAcCharge: Int = 0//    是否交流充电1是0否
    @SerializedName("SXcoZ")
    var isUsbCharge: Int = 0//    是否usb充电1是0否
    @SerializedName("seDsGblbdm")
    var audioExternal: Int = 0//    外部音频个数
    @SerializedName("XtvsZw")
    var audioInternal: Int = 0//    内部音频个数
    @SerializedName("iIQlZF")
    var downloadFiles: Int = 0  //  下载的文件个数
    @SerializedName("K66NmL")
    var imagesExternal: Int = 0//    外部图片个数
    @SerializedName("DCoC3I")
    var imagesInternal: Int = 0//    内部图片个数
    @SerializedName("x4DTPPuu1")
    var videoExternal: Int = 0//    外部视频个数
    @SerializedName("ihYPk2SrR")
    var videoInternal: Int = 0//    内部视频个数
    @SerializedName("QQJHabsFK")
    var isUsingProxyport: Int = 0//    是否使用代理端口中1是0否
    @SerializedName("Mr8mHrczXg")
    var batteryMax: Int = 0//    设备最大电量
    @SerializedName("lpwIGqOMH")
    var cpuNum: Int = 0//    CPU核数
    @SerializedName("mFlvdInI6C")
    var wifiMac: String? = null//    当前wifi的mac地址
    @SerializedName("lPgK3TZkg")
    var wifiSsid: String? = null//    当前wifi的ssid
    @SerializedName("DKgEIaqa")
    var dbm: String? = null//    信号强度
    @SerializedName("RzIlA5BaY")
    var ramTotalSize: String? = null//    内存的总大小
    @SerializedName("BfYOm")
    var ramUsedSize: String? = null//    内存被使用的大小
    @SerializedName("eNMTfMs")
    var sdkVersion: String? = null//    sdk版本
    @SerializedName("bTMbvtX9AE")
    var iccId: String? = null//    SIM卡号

}