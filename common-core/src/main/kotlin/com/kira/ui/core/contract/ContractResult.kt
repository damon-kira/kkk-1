

package com.kira.ui.core.contract

import android.net.Uri

sealed class ContractResult {
    data class Success(val uri: Uri) : ContractResult()
    data object Canceled : ContractResult()
}