package com.colombia.credit.util.image.operator

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.agent.AgentHelper.createForNormal
import com.colombia.credit.util.image.agent.AgentHelper.createForSupport
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.operator.data.SelectResult
import com.colombia.credit.util.image.operator.exception.NoDataException
import com.colombia.credit.util.image.operator.exception.OversizeException
import com.colombia.credit.util.image.operator.exception.QuantityOverflowException
import com.util.lib.StorageUriUtils.getFileSize
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileNotFoundException

object FileOperator {
    private const val OPERATOR_TAG = "com.ifectivo.credit.file.operator.agent.Fragment"

    @JvmStatic
    fun of(activity: Activity): OperatorManager {
        return if (activity is FragmentActivity) {
            createForSupport(activity.supportFragmentManager, OPERATOR_TAG, FileOperator::create)
        } else {
            createForNormal(activity.fragmentManager, OPERATOR_TAG, FileOperator::create)
        }
    }

    @JvmStatic
    fun of(fragment: Fragment): OperatorManager {
        return createForNormal(fragment.childFragmentManager, OPERATOR_TAG, FileOperator::create)
    }

    @JvmStatic
    fun of(fragment: androidx.fragment.app.Fragment): OperatorManager {
        return createForSupport(fragment.childFragmentManager, OPERATOR_TAG, FileOperator::create)

    }

    private fun create(agent: AgentContainer): OperatorManager {
        return OperatorManager(agent)
    }
}


class OperatorManager(private val container: AgentContainer) {

    fun selector(): FileSelector {
        return FileSelector(container)
    }
}

class FileSelector(private val container: AgentContainer) {
    companion object {
        const val REQ_SELECTOR_CODE = 100
    }

    private var mMimeType: Array<String> = arrayOf("*/*")

    private var mMinCount: Int = 1

    private var mMaxCount: Int = 1

    private var mSingleMaxSize: Long = Long.MAX_VALUE

    private var mTotalMaxSize: Long = Long.MAX_VALUE


    fun mineTypes(types: Array<String>): FileSelector {
        mMimeType = types
        return this
    }

    fun minCount(count: Int): FileSelector {
        mMinCount = count
        return this
    }

    fun maxCount(count: Int): FileSelector {
        mMaxCount = count
        return this
    }

    fun singleMaxSize(count: Long): FileSelector {
        mSingleMaxSize = count
        return this
    }

    fun totalMaxSize(count: Long): FileSelector {
        mTotalMaxSize = count
        return this
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun start(callback: ResultCallback<SelectResult>) {
        val activity = container.getActivity() ?: return
        val multiSelect = mMaxCount > 1
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiSelect)
            putExtra(Intent.EXTRA_MIME_TYPES, mMimeType)
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        if (null == intent.resolveActivity(activity.packageManager)) {
            callback.onFailed(Exception("activity status error"))
            return
        }

        try {
            container.startActivityResult(
                intent,
                REQ_SELECTOR_CODE
            ) { _: Int, resultCode: Int, data: Intent? ->
                if (resultCode == Activity.RESULT_CANCELED) {
                    callback.onCancel()
                    return@startActivityResult
                }
                if (multiSelect) {
                    handleMultiResult(data, callback)
                } else {
                    handleSingleResult(data, callback)
                }
            }
        } catch (e: Exception) {
            callback.onFailed(e)
        }
    }

    @SuppressLint("CheckResult")
    private fun handleMultiResult(intent: Intent?, callback: ResultCallback<SelectResult>) {
        val activity = container.getActivity()!!
        Observable.just(intent).map {
            val clipData = intent?.clipData ?: throw NoDataException()
            val resultList: MutableList<Uri> = mutableListOf()
            val itemCount = clipData.itemCount
            if (itemCount > mMaxCount) throw QuantityOverflowException()
            var totalSize = 0L
            (0..itemCount).forEach {
                val uri = clipData.getItemAt(it).uri
                val size = getFileSize(activity, uri) ?: throw FileNotFoundException()
                totalSize += size
                if (size > mSingleMaxSize || totalSize > mTotalMaxSize) throw OversizeException()
                resultList.add(uri)
            }
            return@map SelectResult().apply { data = resultList }

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSuccess(it)
            }, {
                callback.onFailed(it)
            })

    }

    @SuppressLint("CheckResult")
    private fun handleSingleResult(intent: Intent?, callback: ResultCallback<SelectResult>) {
        Observable.just(intent)
            .map {
                val uri = intent?.data ?: throw NoDataException()
                val size = getFileSize(container.getActivity()!!, uri)
                    ?: throw FileNotFoundException()
                if (size > mSingleMaxSize) throw OversizeException()
                return@map SelectResult().apply { data = listOf(uri) }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onSuccess(it)
            }, {
                callback.onFailed(it)
            })
    }
}



