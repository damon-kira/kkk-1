package com.kira.learning.xml.modules.richview

import com.kira.richtext.Attachment


class ExampleAttachment(
    private val mText: String?,
    private val mId: String?,
    private val mIsImage: Boolean,
    private val mUrl: String?
) : Attachment() {
    override fun getUrl(): String? {
        return mUrl
    }

    override fun getText(): String? {
        return mText
    }

    override fun getAttachmentId(): String? {
        return mId
    }

    override fun isImage(): Boolean {
        return mIsImage
    }
}
