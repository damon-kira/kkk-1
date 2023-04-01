package com.colombia.credit.util.image.agent

import androidx.fragment.app.FragmentManager

object AgentHelper {

    @JvmStatic
    private fun addAgentSupportFragment(fm: FragmentManager, tag: String): AgentContainer {
        val fra = AgentSupportFragment();
        try {
            fm.beginTransaction()
                .add(fra, tag)
                .commitAllowingStateLoss()
            fm.executePendingTransactions()
        } catch (e: java.lang.Exception) {
        }
        return fra
    }

    @JvmStatic
    fun addAgentFragment(fm: android.app.FragmentManager, tag: String): AgentContainer {
        val fra = AgentFragment()
        try {
            fm.beginTransaction()
                .add(fra, tag)
                .commitAllowingStateLoss()
            fm.executePendingTransactions()
        } catch (e: java.lang.Exception) {

        }
        return fra
    }


     fun <T> createForSupport(
        fm: androidx.fragment.app.FragmentManager,
        tag: String,
        creator: (AgentContainer) -> T
    ): T {
        val agentFragment = fm.findFragmentByTag(tag) as AgentContainer?
        val container: AgentContainer = agentFragment ?: addAgentSupportFragment(fm, tag)
        return creator(container)
    }

    fun <T> createForNormal(
        fm: android.app.FragmentManager,
        tag: String,
        creator: (AgentContainer) -> T
    ): T {
        val agentFragment = fm.findFragmentByTag(tag) as AgentContainer?
        val container: AgentContainer = agentFragment ?: addAgentFragment(fm, tag)
        return creator(container)
    }

}

