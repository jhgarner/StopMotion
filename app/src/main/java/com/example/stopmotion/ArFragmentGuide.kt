package com.example.stopmotion

import android.widget.TextView
import com.google.ar.core.Pose
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment
import kotlin.math.atan2

class ArFragmentGuide : ArFragment() {
    public var cameraPos: Pose? = null
    public var scenePos: Pose? = null
    public var messageBox: TextView? = null

    //fun getTheta(): Float {
        //val cPos = cameraPos!!.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
        //val sPos = scenePos!!.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
        //val fPos = arSceneView.arFrame!!.camera.pose.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
//
        //atan2(cPos[])
    //}

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        if (cameraPos != null && scenePos != null && messageBox != null) {
            val cPos = cameraPos!!.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
            val sPos = scenePos!!.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))


            val currentPosFrame = arSceneView.arFrame!!.camera.pose.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))

            var message = ""
            if (cPos[0] - currentPosFrame[0] < 0) {
                message += "-X "
            } else {
                message += "+X "
            }
            if (cPos[1] - currentPosFrame[1] < 0) {
                message += "-Y "
            } else {
                message += "+Y "
            }
            if (cPos[2] - currentPosFrame[2] < 0) {
                message += "-Z "
            } else {
                message += "+Z "
            }
            message += "(" + (cPos[0] - sPos[0]) + ", " + (cPos[1] - sPos[1]) + ", " + (cPos[2] - sPos[2]) + ") "
            message += "(" + (cPos[0]) + ", " + (cPos[1]) + ", " + (cPos[2]) + ")"
            message += "(" + (currentPosFrame[0]) + ", " + (currentPosFrame[1]) + ", " + (currentPosFrame[2]) + ")"
            messageBox!!.text = message
        }
    }
}