package com.colombia.credit.module.home

object UserStatus {
    const val STATUS_FIRST = "1"
    const val STATUS_REPEAT = "2"
    const val STATUS_BLACK = "3" // 只有复贷用户来黑，首贷不会
}

// 00：新用户确定额度,
// 01：订单审核中,
// 02：待还款,
// 03: 逾期,
// 04: 拒单，
// 05：认证未通过，新用户起始页，
// 06: 被拒到期
// 07: 复借客户没有待还订单、未确定订单
// 08：复借客户有待还订单，没有待确定订单
// 09：复借客户没有待还订单，有待确定订单
// 10：复借客户既有待还订单也有待确定订单
object OrderStatus {
    const val STATUS_FIRST_CONFIRM = "00" // 新用户确定额度
    const val STATUS_REVIEW        = "01" // 订单审核中
    const val STATUS_REPAY         = "02" // 待还款
    const val STATUS_OVERDUE       = "03" // 逾期
    const val STATUS_REJECT        = "04" // 拒绝
    const val STATUS_FIRST_PRODUCT = "05" // 首贷首页
    const val STATUS_REFUSED_EXPIRE= "06" // 被拒到期
    const val STATUS_REPEAT1       = "07" // 被拒到期
    const val STATUS_REPEAT2       = "08" // 被拒到期
    const val STATUS_REPEAT3       = "09" // 被拒到期
    const val STATUS_REPEAT4       = "10" // 被拒到期
}