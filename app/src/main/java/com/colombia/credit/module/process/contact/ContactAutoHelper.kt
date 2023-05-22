package com.colombia.credit.module.process.contact

import com.colombia.credit.databinding.ActivityContactInfoBinding
import com.colombia.credit.module.process.AbsAutoCheckHelper
import com.colombia.credit.view.baseinfo.AbsBaseInfoView

abstract class ContactAutoHelper(vb: ActivityContactInfoBinding): AbsAutoCheckHelper<ActivityContactInfoBinding>(vb) {

    companion object {
        const val ITEM_RELATIONSHIP = 0
        const val ITEM_CONTACT1 = 1
        const val ITEM_CONTACT2 = 2
    }

    override fun checkIndexResult(index: Int): Boolean = index > ITEM_CONTACT2

    override fun checkByValue(index: Int) {
        when (index) {
            ITEM_RELATIONSHIP -> checkRelationShip()
            ITEM_CONTACT1 -> checkContact(index, vb.bivContact1)
            ITEM_CONTACT2 -> checkContact(index, vb.bivContact2)
        }
    }

    private fun checkRelationShip() {
        if (!checkInfoEmpty(vb.bivRelationship)) {
            checkNextView()
            return
        }
        showItemDialog(ITEM_RELATIONSHIP)
    }

    private fun checkContact(index: Int, infoView: AbsBaseInfoView) {
        if (!checkInfoEmpty(infoView)) {
            checkNextView()
            return
        }
        showItemDialog(index)
    }

}