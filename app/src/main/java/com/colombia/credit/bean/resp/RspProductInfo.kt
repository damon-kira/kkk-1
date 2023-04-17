package com.colombia.credit.bean.resp

class RspProductInfo {
    val yqGhrjOF2: String? = null // 最高可借金额，实际还款金额，借款金额
    val HyulExS1ei: String? = null //客户名字
    val cusTell: String? = null //客户号码
    val WTvE5G: Int = 0// 借款天数
    val ZXEUWfOy: String? = null//订单id
    val EqyO: String = "1"// 客户类型 1 :新客户 0 老客户 2 被拉黑客户
    val Wg5u: String? = null //客户类型 A :正常用户 G:谷歌审查用户

    // 状态 00：新用户确定额度,01：订单审核中,02：待还款,03:逾期,04:拒单，05：认证未通过，新用户起始页，06:被拒到期 07:复借客户没有待还订单、未确定订单  08：复借客户有待还订单，没有待确定订单  09：复借客户没有待还订单，有待确定订单 10：复借客户既有待还订单也有待确定订单
    val xXkO: String = "00"
    val vzXq3u: String? = null// 还款日期
    val y5MbVG: Int = 0// 总期数
    val y7GxqEUf: Int = 0// 当前期数
    val v3ItXF: Int = 0 // 逾期天数
    val K1v0Pz: String? = null// 下一次申请时间
    val A04fSYQdHM: Int = 0// 最大勾选笔数
    val yMiEwn3: String? = null // 银行卡号
    val swOwF0KJ: Long = 0// 弹框秒数
    val GqGV3L: Int = 1// 倒计时开关 1 开 0 关
    val fyEV: ArrayList<FirstConfirmInfo>? = null // 新客户确定额度首页
    val jBRR: ArrayList<RepeatProductInfo>? = null // 老客户首页
    val gQ1J: ArrayList<RepeatRepayInfo>? = null // 老客户待还
    val Jg4g2: ArrayList<RepeatWaitConfirmInfo>? = null // 老客户待确认列表
    val RdJ7nJ: String? = null // y3 新增字段
}

//"handAt":100,//到手金额
//"applyAt":100,//贷款金额,
//"sumPer": 2,//期数
//"backAt":110,// 应还金额
//"moreAt":222,//利息
//"applyInteger":"7,14,90,108",//贷款周期,
//"reBackDate":"2023-05-01",//还款时间
//"applyId":23,// 产品id
//"applyCode":"RTS"// 产品code
// 首贷产品信息
class FirstConfirmInfo {
    val u5kCNqk: String? = null//到手金额
    val RIoDBuyjO: String? = null//贷款金额,
    val y5MbVG: String? = null//期数
    val b6O2Joc: String? = null// 应还金额
    val ihm3G2: String? = null//利息
    val WTvE5G: String? = null//贷款周期  "7,14,90,108"
    val vzXq3u:String? = null // 还款时间"2023-05-01"
    val ZXEUWfOy: String? = null// 产品id
    val XbCqhjDV:String? = null// 产品code
}

//"rangeMaxAt":20000,//范围金额最大值
//"rangeminAt":1000, //范围金额最小值
//"dayRate":"0.1%",// 日利息
//"maxDay":365,// 周期最大值
//"minDay":180,// 周期最小值
//"prNme":"XXX",// 产品名称
//"prUrl":"xxx",// 产品logo
//"prCode":"RST",// 产品编码
//"isCheck": 1// 是否选中 1：选中 0：未选中,
////新增
//"prTag":"1,2,3"
// 复贷产品信息
class RepeatProductInfo {
    val g7tzi: Int = 0//范围金额最大值
    val sF1DFWU: Int = 0 //范围金额最小值
    val xXgaK4: String? = null // "0.1%" 日利息
    val cQ75eX5: Int = 0 //周期最大值
    val D9hR: Int = 0 // 周期最小值
    val S9ig78H: String? = null // 产品名称
    val Gk9MGh: String? = null // 产品logo url
    val eqOEs: String? = null // RST 产品编码
    val RXYz: Int = 0 // // 是否选中 1：选中 0：未选中
    val ir3MCCmbF3: String? = null //"prTag":"1,2,3"
}

//"notRepayCount":2,//未还订单数
//"notRepay":1000, //未还金额
//"maxAmount":52000,// 待还借金额
//"applyIds":[11111, 222]// 申请id
// 复贷还款信息
class RepeatRepayInfo {
    val AMGH9kXswv: Int = 0 //未还订单数
    val RPBJ47rhC: Int = 0 //未还金额
    val RYtVvxJH: Int = 0 //待还借金额
    val QLPGXTNU: ArrayList<String>? = null // 申请id [11111, 222]
}

//"maxApplyAt":20000,//范围金额最大值
//"bathId":"",// 批次号
//"notConfirmList":[
//{
//    "prNme":"xxx",//产平名称
//    "prUrl":"xxxx" //logo
//}
//]
// 复贷待确认订单
class RepeatWaitConfirmInfo{
    val yqGhrjOF2: Int = 0//范围金额最大值
    val tQXtG0FYb: String? = null // 批次号
    val I4Ai: ArrayList<WaitConfirm>? = null // 产品信息
}

class WaitConfirm{
    val S9ig78H: String? = null//产品名称
    val Gk9MGh: String? = null//产品logo
}