package com.camera.lib

import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.mexico.camera.cameraview.ICamera

/**
 *@author zhujun
 *@description:
 *@date : 2022/7/27 10:09 上午
 */
object CameraFactory {

    /**
     * type:相机类型
     * flameLayout：父布局
     * cameraOrientation：摄像头朝向  后置 FACING_BACK 前置 FACING_FRONT
     */
    operator fun invoke(
        type: CameraType,
        activity: AppCompatActivity,
        cameraView: CameraView,
        cameraOrientation: Int,
        screenOrientation: Int
    ): ICamera {
        return when (type) {
            CameraType.CameraOne -> {
                cameraView.setCameraType(CameraType.CameraOne)
                CameraOneManager(activity, cameraView.curView as TextureView, cameraOrientation, screenOrientation)
            }
            CameraType.CameraX -> {
                cameraView.setCameraType(CameraType.CameraX)
                CameraXManager(activity, cameraView.curView as PreviewView, cameraOrientation)
            }
        }
    }

}

enum class CameraType {
    CameraOne, CameraX
}