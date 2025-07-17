

package com.kira.ui.core.contract

import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.fragment.app.Fragment

class OpenFileContract(
    fragment: Fragment,
    private val onResult: (ContractResult) -> Unit,
) {

    private val openDocument = fragment.registerForActivityResult(OpenDocument()) { result ->
        if (result != null) {
            onResult(ContractResult.Success(result))
        } else {
            onResult(ContractResult.Canceled)
        }
    }

    fun launch(vararg mimeTypes: String) {
        openDocument.launch(arrayOf(*mimeTypes))
    }

    companion object {
        const val JSON = "application/json"
        const val FONT = "font/*"
        const val X_FONT = "application/x-font-ttf"
        const val OCTET_STREAM = "application/octet-stream"
        const val PEM = "application/x-pem-file"
        const val ANY = "*/*"
    }
}