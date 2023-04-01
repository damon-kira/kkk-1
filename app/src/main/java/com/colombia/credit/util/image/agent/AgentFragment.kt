package com.colombia.credit.util.image.agent

import android.annotation.SuppressLint
import android.content.Intent
import android.util.ArrayMap
import com.colombia.credit.util.image.callback.ContainerCallback


@SuppressLint("ValidFragment")
class AgentFragment : android.app.Fragment(), AgentContainer {

    private val callbacks = ArrayMap<Int, ContainerCallback>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = callbacks.remove(requestCode) ?: return
        callback(requestCode, resultCode, data)
    }

    override fun startActivityResult(intent: Intent, requestCode: Int, containerCallback: ContainerCallback?) {
        callbacks[requestCode] = containerCallback
        startActivityForResult(intent, requestCode)
    }

}
