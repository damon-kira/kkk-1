package com.kira.learning.utils.image.agent

import android.app.Activity
import android.content.Intent
import com.kira.learning.utils.image.callback.ContainerCallback

interface AgentContainer {

    fun getActivity(): Activity?

    fun startActivityResult(intent: Intent, requestCode: Int, containerCallback: ContainerCallback)

}


