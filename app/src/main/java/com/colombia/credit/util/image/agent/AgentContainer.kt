package com.colombia.credit.util.image.agent

import android.app.Activity
import android.content.Intent
import com.colombia.credit.util.image.callback.ContainerCallback

interface AgentContainer {

    fun getActivity(): Activity?

    fun startActivityResult(intent: Intent, requestCode: Int, containerCallback: ContainerCallback?)

}


