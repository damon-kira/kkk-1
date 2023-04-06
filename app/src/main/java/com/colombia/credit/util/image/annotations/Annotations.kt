package com.colombia.credit.util.image.annotations

import androidx.annotation.IntDef
import com.camera.lib.CameraOneUtils.Companion.CAMERA_BACK
import com.camera.lib.CameraOneUtils.Companion.CAMERA_FRONT
import com.colombia.credit.util.image.annotations.PickRange.Companion.PICK_CONTENT
import com.colombia.credit.util.image.annotations.PickRange.Companion.PICK_DICM


@IntDef(CameraFace.FRONT, CameraFace.BACK)
@Retention(AnnotationRetention.SOURCE)
annotation class CameraFace {
    companion object {
        const val FRONT = 0 //前端摄像头
        const val BACK = 1 // 后端摄像机
    }
}

@IntDef(PICK_CONTENT, PICK_DICM)
@Retention(AnnotationRetention.SOURCE)
annotation class PickRange {
    companion object {
        const val PICK_CONTENT = 0
        const val PICK_DICM = 1
    }
}

@IntDef(ImageType.ALL, ImageType.JPEG, ImageType.PNG, ImageType.GIF)
@Retention(AnnotationRetention.SOURCE)
annotation class ImageType {
    companion object {
        const val ALL = 0
        const val JPEG = 1
        const val PNG = 2
        const val GIF = 3
    }
}

@IntDef(CAMERA_BACK, CAMERA_FRONT)
@Retention(AnnotationRetention.SOURCE)
annotation class CapturePhotoType

@IntDef(PicType.PIC_BACK, PicType.PIC_FRONT)
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PicType {
    companion object {
        const val PIC_BACK = 1     // 反面
        const val PIC_FRONT = 2    // 正面
    }
}