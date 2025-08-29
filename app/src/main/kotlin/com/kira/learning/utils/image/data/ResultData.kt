package com.kira.learning.utils.image.data

import android.net.Uri

open class ResultData {
    var uri: Uri? = null
}

open class CompressResult : ResultData() {
    var sourceUri: Uri? = null
}