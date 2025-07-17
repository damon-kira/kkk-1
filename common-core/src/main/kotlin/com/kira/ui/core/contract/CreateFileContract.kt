

package com.kira.ui.core.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment

class CreateFileContract(
    fragment: Fragment,
    private val onResult: (ContractResult) -> Unit,
) {

    private var mimeType = "*/*"

    /**
     * [androidx.activity.result.contract.ActivityResultContracts.CreateDocument]
     */
    private val createDocument = fragment.registerForActivityResult(
        object : ActivityResultContract<String, Uri?>() {
            override fun createIntent(context: Context, input: String): Intent {
                return Intent(Intent.ACTION_CREATE_DOCUMENT)
                    .setType(mimeType)
                    .putExtra(Intent.EXTRA_TITLE, input)
            }
            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
            }
            override fun getSynchronousResult(
                context: Context,
                input: String
            ): SynchronousResult<Uri?>? = null
        }
    ) { result ->
        if (result != null) {
            onResult(ContractResult.Success(result))
        } else {
            onResult(ContractResult.Canceled)
        }
    }

    fun launch(title: String, mimeType: String) {
        this.mimeType = mimeType
        createDocument.launch(title)
    }

    companion object {
        const val JSON = "application/json"
        const val TEXT = "text/*"
    }
}