package com.common.lib.net

/**
 * Created by weisl on 2019/8/13.
 */
object ResponseCode {

    const val FORCED_UPDATE_CODE = 800800 //强制升级

    const val FAILED_CHECK = -10
    const val SUCCESS = 0
    const val LOADING = -10
    const val FAILED = -1

    const val TAG = "okhttp"
    const val SUCCESS_CODE = 0//接口请求成功码
    const val OTHER_ERROR_CODE = -111//其他未定义错误
    const val SSL_ERROR_CODE = 120 // CA证书失效
    const val REQUEST_ERROR_CODE = -2//请求失败
    const val PAYTM_ERROR_CODE = -3//paytm支付错误
    const val INVALIDTOKEN = 400114//token过期和无效
    const val INVALID_AUTH_CODE = 400116 //验证码错误
    const val API_FREQUENTLY = 400117//请求接口过于频繁  获取验证码 和 复贷获取验证码接口
    const val LIMIT_24_HOUT = 400120//手机号使用受到限制,请求验证码达到上限,目前是每种类型24小时内6次
    const val IDENDITY_PHONE_REPEAD_BIND = 400132 // 身份证号和手机号重复绑定
    const val SERVICE_ERROR_CODE = 500111//服务器错误，出现严重问题的特殊错误码
    const val ERROR_TWO_IMAGES_COMPARE = 400134 //复贷上传手持照片两次比对结果不一样
    const val ERROR_ORIGIN_IMAGES_NO_CACHE = 400133 //复贷上传手持照片原有照片不存在
    const val ERROR_NOT_PRODUCT = 400128 // 产品不存在

    const val ERROR_VERCODE_FREQUENTLY = 400140  //验证码请求过于频繁
    const val ERROR_PASSWORD_UNSET = 400146  //找回密码时，账号未设置密码

    const val ERROR_ORDER_NOTEXIST = 400147 //order不存在 无需申请
    const val LIMIT_VOICE_VERIFY_DISABLE = 400150//语音验证码不可用
    const val LIMIT_VOICE_24_HOUR = 400152//手机号使用受到限制,请求语音验证码达到上限,目前是每种类型24小时内6次
    const val ERROR_MODFIY_BANK_FAILED = 400153   // 放款失败，展示修改银行卡号界面，用户尝试修改银行卡号，但是失败了
    const val ERROR_MODEIY_BANK_VA_FAILED = 400155 //还款引导页 修改支付银行失败

    const val ERROR_CHECK_BANK_INFO_FAILED = 400169 // 检查银行信息错误
    const val ERROR_CHECK_BANK_INFO_OUT = 410170 // 检查银行信息次数已用光
    const val ERROR_BANK_NAME_NOT_MATCHING = 402003 // 银行账户和姓名不匹配

    const val ERROR_IFSC_CODE_INVALID = 400194 // IFSC号码格式不正确
    const val ERROR_BANK_ACCOUNT_FORMAT_ERROR = 400195 // 银行卡号码格式不正确
    const val ERROR_POSTCODE_NOT_EXIST = 400196 // 邮编不存在
    const val ERROR_IFSC_NOT_EXIST = 400197 // IFSC号码不存在
    const val ERROR_IFSC_INFO_NOT_EXIST = 400198 // 填写的IFSC信息错误

    const val ERROR_FIRST_NAME_INVALID = 400171 // FirstName格式不正确
    const val ERROR_LAST_NAME_INVALID = 400172 // LastName格式不正确
    const val ERROR_MIDDLE_NAME_INVALID = 402010 // MiddleName格式不正确
    const val ERROR_PAN_NUMBER_INVALID = 400173 // Pan 卡格式不正确
    const val ERROR_AADHAR_INVALID = 400174 // Aadhaar 卡格式不正确
    const val ERROR_EMAIL_INVALID = 400175 // 邮箱格式不正确
    const val ERROR_POSTCODE_INVALID = 400176 // 邮编格式不正确
    const val ERROR_GENDER_INVALID = 400178 // 性别超出范围
    const val ERROR_EDUCATION_INVALID = 400179 // 学历超出范围
    const val ERROR_MARITAL_STATUS_INVALID = 400180 // 婚姻状态超出范围
    const val ERROR_BIRTHDAY_INVALID = 400181 // 生日格式不正确
    const val ERROR_PAN_NUMBER_OCCUPIED = 400182 // Pan 卡已被占用
    const val ERROR_AADHAR_OCCUPIED = 400183 // Aadhaar 卡已被占用
    const val ERROR_PAN_NOT_EXIST = 400184 // Pan 卡不存在
    const val ERROR_PAN_MISMATCHED = 400185 // Pan 卡与姓名不匹配
    const val ERROR_PAN_STATUS_INEDITABLE = 400186 // 此状态不允许编辑Pan卡和姓名
    const val ERROR_PHONE_INVALID = 400187 // 电话号码格式不正确
    const val ERROR_NAME_INVALID = 400188 // 姓名格式不正确
    const val ERROR_RELATION_INVALID = 400189 // 关系类型超出范围
    const val ERROR_WORKTYPE_INVALID = 400190 // 工作类型超出范围
    const val ERROR_SALARY_INVALID = 400191 // 此状态不允许月薪
    const val ERROR_COMPANY_ADDRESS_INVALID = 400193 // 公司地址格式不正确

    const val ERROR_PAN_OVER_VERIFY_NUM = 402002 // PAN卡验证次数超限
    const val ERROR_LOST_REQUIRED_PARAMETERS = 400110 // 缺少必要参数
    const val ERROR_MOBILE_HAS_REGISTERED = 400141    //手机号已被注册
    const val ERROR_MOBILE_NOT_REGISTERED = 400142    //手机号未被注册
    const val ERROR_ACCOUNT_LOCKED = 400145           //账号被锁定
    const val ERROR_INVALID_PASSWORD = 400143         //密码无效
    const val ERROR_INVALID_OLD_PASSWORD = 400144      //旧密码无效
    const val ERROR_NEW_PWD_MATCH_THE_OLD = 401003//新旧密码一致

    const val ERROR_CREATE_VA_FAILED       = 410171 // va账户创建失败

    const val ERROR_FACEBOOK_ALREADY_OTHER         = 400806 // facebook已绑定其它账号
    const val ERROR_FACEBOOK_ALREADY_REGISTER      = 400803 // facebook已经注册
    const val ERROR_FACEBOOK_ALREADY_BINDING_OTHER = 400804 // 已经绑定其它账户

    const val ERROR_GOOGLE_ALREADY_REGISTER         = 400906 // google 已经注册

    const val ERROR_BANK_ACCOUNT_EXIST = 402006 // 银行卡账号已存在

    const val ERROR_BANK_CARD_BINDED = 401100 //校验该银行卡已被其他账户绑定
    const val ERROR_EMAIL_BINDED = 401101 //校验该email已被其他账户绑定

    const val ERROR_SAME_BANK_INFO = 402007 // 修改银行卡,未更新提示code

    const val ERROR_PAN_CARD_NOT_UPLOAD          = 410174  // Pan卡照片未上传
    const val ERROR_ADDRESSPROOF_NOT_UPLOAD      = 410175  // 地址证明照片未上传
    const val ERROR_SELFIE_NOT_UPLOAD            = 410176  // 自拍照未上传

    const val ERROR_COUPON_USE_FAILED           = 400159 //优惠券使用失败
    const val ERROR_COUPON_INVALID              = 400203  //优惠券已过期，不可用

    const val ERROR_ACTIVITIES_FAILED           = 400401 // 查询浮窗活动信息失败
    const val ERROR_BANNER_FAILED               = 400501 // 查询banner活动信息失败


    /************   进件项   *************/
    const val ERROR_BANK_NO_INVALLID            = 450003  //银行卡号有误
    const val ERROR_ADDRESS_INVALID             = 400177 // 住址格式不正确
    const val ERROR_RFC_CODE_INVALID            = 400201 // RFC码填写错误
    const val ERROR_DETAIL_ADDRESS_INVALLID     = 450011  //详细地址有误

    const val ERROR_COMPANY_NAME_INVALID        = 400192 // 公司名字格式不正确
    const val ERROR_WORK_LENGTH_INVALID         = 450005 // 工作时长填写有误
    const val ERROR_SALARY_DAY_INVALID          = 450006 // 发薪日１填写有误
    const val ERROR_SALARY_DAY2_INVALID         = 450007 // 发薪日2填写有误
    const val ERROR_COMPANY_TEL_INVALID         = 450008 // 公司电话填写有误

    const val ERROR_PHONE_NUM1_INVALID          = 402013 // 联系人１号码不正确
    const val ERROR_PHONE_NUM2_INVALID          = 402014 // 联系人2号码不正确
    const val ERROR_PHONE_NUM3_INVALID          = 402018 // 联系人3号码不正确
    const val ERROR_TWO_CONTACT_PHONE_SAME      = 402004 // 2个联系人手机号相同

    const val ERROR_LOAN_AMOUNT_TOO_LARGE       = 400911 //借款金额过大
    const val ERROR_CANCEL_AUTO_CONFIRM         = 403002 //请求取消自动确认放款
    const val ERROR_CONFIRM_LOAN_ERROR          = 400131 // 确认放款失败，已自动确认放款

    const val ERROR_INCREASE_COUPON_UNAVAILABLE   = 510006 // 提额券不符合约束条件
    const val ERROR_REDUCE_COUPON_UNAVAILABLE     = 510007 // 减免类的券不符合约束条件
}