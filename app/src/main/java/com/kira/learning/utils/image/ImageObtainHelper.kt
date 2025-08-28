package com.kira.learning.utils.image

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.kira.learning.utils.image.agent.AgentContainer
import com.kira.learning.utils.image.agent.AgentHelper.createForNormal
import com.kira.learning.utils.image.agent.AgentHelper.createForSupport


object ImageObtainHelper {

    private const val PICK_TAG = "com.mexico.loan.image.pick.agent.Fragment"

    @JvmStatic
    fun createAgent(activity: Activity): FunctionManager {
        if (activity is FragmentActivity) {
            return createForSupport(
                activity.supportFragmentManager,
                PICK_TAG,
                ImageObtainHelper::create
            )
        } else {
            return createForNormal(activity.fragmentManager, PICK_TAG, ImageObtainHelper::create)
        }
    }

    @JvmStatic
    fun createAgent(fragment: android.app.Fragment): FunctionManager {
        return createForNormal(fragment.childFragmentManager, PICK_TAG, ImageObtainHelper::create)
    }

    @JvmStatic
    fun createAgent(fragment: androidx.fragment.app.Fragment): FunctionManager {
        return createForSupport(fragment.childFragmentManager, PICK_TAG, ImageObtainHelper::create)
    }

    private fun create(agent: AgentContainer): FunctionManager {
        return FunctionManager(agent)
    }


}











