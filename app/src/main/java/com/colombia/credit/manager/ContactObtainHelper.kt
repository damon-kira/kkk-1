package com.colombia.credit.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.colombia.credit.R
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.PhoneAndName
import com.colombia.credit.manager.ObtainContactAgent.Companion.REQ_CONTACT_CODE


private typealias ContactCallback = (resultCode: Int, data: PhoneAndName?) -> Unit

object ContactObtainHelper {

    private const val CONTACT_TAG = "com.mexico.loan.contact.agent.Fragment"

    fun createObtainContact(fragment: Fragment): ObtainContactAgent {
        return createForSupport(fragment.childFragmentManager)
    }

    fun createObtainContact(activity: AppCompatActivity): ObtainContactAgent {
        return createForSupport(activity.supportFragmentManager)
    }

    private fun createForSupport(fm: androidx.fragment.app.FragmentManager): ObtainContactAgent {
        val agentFragment = fm.findFragmentByTag(CONTACT_TAG) as AgentXFragment?
        val container: IContactContainer = agentFragment ?: addAgentXFragment(fm)
        return ObtainContactAgent(container)
    }

    private fun addAgentXFragment(fm: androidx.fragment.app.FragmentManager): AgentXFragment {
        val fra = AgentXFragment();
        try {
            fm.beginTransaction()
                .add(fra, CONTACT_TAG)
                .commitAllowingStateLoss()
            fm.executePendingTransactions()
        } catch (e: java.lang.Exception) {
        }
        return fra
    }
}

class ObtainContactAgent(private val container: IContactContainer) : IContactContainer {
    companion object {
        const val REQ_CONTACT_CODE = 11111
    }

    override fun openContact(callback: ContactCallback) {
        container.openContact(callback)
    }
}

interface IContactContainer {
    fun openContact(callback: ContactCallback)
}


class AgentXFragment : Fragment(), IContactContainer {

    private var mCallback: ContactCallback? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = mCallback ?: return
        if (resultCode == Activity.RESULT_OK) {
            context?.let { ctx ->
                val result = ContactHelper.queryContactInfo(ctx, data?.data)
                callback.invoke(resultCode, result)
            }
        } else {
            callback.invoke(resultCode, null)
        }
    }

    @SuppressLint("IntentReset")
    override fun openContact(callback: ContactCallback) {
        this.mCallback = callback
        val title = getAppContext().getString(R.string.app_name)
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("content://contacts/people")
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
//        if (intent.resolveActivity(getAppContext().packageManager) != null) {
        //fix No Activity found to handle Intent
        try {
            startActivityForResult(intent, REQ_CONTACT_CODE)
        } catch (e: Exception) {
            callback.invoke(0, null)
        }
//        } else {
//            callback.invoke(0, null)
//        }
    }
}