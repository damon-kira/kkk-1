package com.bigdata.lib.bean

import com.google.gson.annotations.SerializedName

class BaseInfo {
    @SerializedName("Z2Zu")
    var isSwitchPages: Int = 0// 	认证时，是否有切换去别的页面，只要有一次就是传1是0否
    @SerializedName("nvlg")
    var readPrivacyAgreementTime: Long = 0// 	阅读隐私协议最长时间

    var readPrivacyAgreementTimes: Int = 0 //    阅读隐私协议次数
    var registWifi: String? = null//    注册时wifi
    var registIp: String? = null//    注册时ip
    var faceCheckWifi: String? = null//    借款人脸时使用的wifi
    var loanRequestWifi: String? = null//    借款时使用的wifi
    var internetType: String? = null //网络类型wifi4G3g
    var bootDate: String? = null//    开机日期保存时间戳精确到秒
    var bootTime: String? = null//    开机时长单位为秒
    var batteryPower: Int = 0//    电池电量0-100
    var isRoot: Int = 0//    是否root1是0否
    var systemVersion: String? = null//    系统版本
    var screenRateLong: Int = 0//    分辨率长
    var screenRateWidth: Int = 0//    分辨率宽

    //  ocr图片exif的JSON如果不存在motherfuck，thisisarealpicture存在既按{ “extractGps”:””, 拍摄时GPS”extractTakeTime”:””, 拍摄日期”extractPohotoSize”:””, 图片内存100kb”extractPohotoPixel”:””, 图片像素100x100”extractTakeDevice”:””拍摄设备 }格式保存
    var ocrPhotoExif: String? = null

    //   人脸识别图片exif的JSON如果不存在motherfuck，thisisarealpicture存在既按{ “extractGps”:””, 拍摄时GPS”extractTakeTime”:””, 拍摄日期”extractPohotoSize”:””, 图片内存100kb”extractPohotoPixel”:””, 图片像素100x100”extractTakeDevice”:””拍摄设备 }格式保存
    var faceCheckExif: String? = null
    var imei: String? = null//    手机序列号
    var imei2: String? = null//手机序列号2
    var gaid: String? = null//广告ID
    var imsi: String? = null//    国际移动用户识别码
    var androidId: String? = null//    安卓ID
    var mac: String? = null//    mac
    var storageTotalSize: String? = null//    设备总容量
    var storageAvailableSize: String? = null//    设备可用余量
    var sdCardTotalSize: String? = null//    sd卡总容量
    var sdCardAvailableSize: String? = null//    sd卡可用余量
    var usageTimeBeforeOrder: Long = 0//    使用期间
    var firstUseAndRequestIntervalTime: Long = 0//    首次使用到申请时间间隔
    var loginAccountEnterTime: Long = 0//    登录手机号输入时长
    var backgroundRecoveryTimes: Int = 0//    当前使用从后台恢复次数
    var chargingStatus: Int = 0//    是否充电状态0否1是
    var simState: Int = 0//    SIM状态1无卡2单卡3双卡
    var timeZone: String? = null//    时区
    var vpn: String? = null//    vpn
    var phoneLanguage: String? = null//    手机语言
    var screenBrightness: Int = 0//    屏幕亮度
    var mcc: String? = null//    mcc
    var mnc: String? = null//    mnc
    var developerStatus: Int = 0//    是否开启开发者状态0否1是
    var addresSimulationApp: Int = 0//    是否安装模拟软件0否1是
    var operators: String? = null//    手机运营商
    var loanPageStayTime: Long = 0//    贷款页面停留时间
    var wifiList: String? = null//    wifi列表json格式：[{ “name”:”ddd”, ”ip”:”163.195.6.6” }]

    //GPS欺诈软件APP列表[{ “firstInstallTime”:1619170976886, ”appName”:”FakeGPS”, ”packageName”:”com.lexa.fakegps”, ”lastUpdateTime”:1619170976886 }]
    var gpsFakeAppList: String? = null
    var advertisingId: String? = null//    广告id
    var photoAlbumListUrl: String? = null//    相册图片列表url
    var isAcCharge: Int = 0//    是否交流充电1是0否
    var isUsbCharge: Int = 0//    是否usb充电1是0否
    var audioExternal: Int = 0//    外部音频个数
    var audioInternal: Int = 0//    内部音频个数
    var downloadFiles: Int = 0  //  下载的文件个数
    var imagesExternal: Int = 0//    外部图片个数
    var imagesInternal: Int = 0//    内部图片个数
    var videoExternal: Int = 0//    外部视频个数
    var videoInternal: Int = 0//    内部视频个数
    var isUsingProxyport: Int = 0//    是否使用代理端口中1是0否
    var batteryMax: Int = 0//    设备最大电量
    var cpuNum: Int = 0//    CPU核数
    var wifiMac: String? = null//    当前wifi的mac地址
    var wifiSsid: String? = null//    当前wifi的ssid
    var dbm: String? = null//    信号强度
    var ramTotalSize: String? = null//    内存的总大小
    var ramUsedSize: String? = null//    内存被使用的大小
    var sdkVersion: String? = null//    sdk版本
    var iccId: String? = null//    SIM卡号
}