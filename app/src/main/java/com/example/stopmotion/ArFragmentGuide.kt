package com.example.stopmotion

import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.ArraySet
import android.view.Surface
import android.widget.TextView
import com.google.ar.core.Pose
import com.google.ar.core.Session
import com.google.ar.core.SharedCamera
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment


//import com.sun.imageio.plugins.jpeg.JPEG


class ArFragmentGuide : ArFragment() {
    public var cameraPos: Pose? = null
    public var scenePos: Pose? = null
    public var messageBox: TextView? = null
    public var sharedCamera: SharedCamera? = null
    public var cameraManager: CameraManager? = null
    public var cameraDevice: CameraDevice? = null
    public var sharedSession: Session? = null
    public var backgroundThread: HandlerThread? = null
    public var backgroundHandler: Handler? = null
    public var h: Handler = Handler()
    public var requestBuilder: CaptureRequest.Builder? = null

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("sharedCameraBackground")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.getLooper())
        //backgroundHandler = Handler()
    }

    override fun getSessionFeatures(): MutableSet<Session.Feature> {
        val sf = ArraySet<Session.Feature>()
        sf.add(Session.Feature.SHARED_CAMERA)
        return sf
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startBackgroundThread()
        cameraManager = activity!!.getSystemService(CAMERA_SERVICE) as CameraManager
        setOnSessionInitializationListener { session: Session? ->
            sharedSession = session
            openCameraForSharing()
        }
        super.onCreate(savedInstanceState)
    }

    //override fun onCreate(savedInstanceState: Bundle?) {
        //val sf = sessionFeatures
        //println("=======")
        //for (s in sf) {
            //println(s)
        //}
        //println("=======")
        //sf.add(Session.Feature.SHARED_CAMERA)
        //super.onCreate(savedInstanceState)
    //}

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


    private fun openCameraForSharing() {
        sharedCamera = sharedSession!!.getSharedCamera()
        // Use callback wrapper.
        try {
            cameraManager!!.openCamera(
                sharedSession!!.getCameraConfig().getCameraId(),
                sharedCamera!!.createARDeviceStateCallback(
                    appDeviceStateCallback, backgroundHandler
                ),
                backgroundHandler
            )
        } catch (e: SecurityException) {
            1 / 0
        }
    }

    val appDeviceStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {

            cameraDevice = camera
            println("Creating!")
            createCameraCaptureSession()
        }

        override fun onDisconnected(camera: CameraDevice) {
            //camera.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            //camera.close()
        }
       }

    private fun createCameraCaptureSession() {
        // Get list of ARCore created surfaces. Required for ARCore tracking.
        requestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)

        val surfaceList: MutableList<Surface> = sharedCamera!!.arCoreSurfaces
        for (surface in surfaceList) {
            requestBuilder!!.addTarget(surface)
        }
        // (Optional) Add a custom CPU image reader surface on devices that support CPU image access.
        //val cpuImageReader: ImageReader = ImageReader::newI();
        //surfaceList.add(cpuImageReader.getSurface());
        // Use callback wrapper.
        cameraDevice!!.createCaptureSession(
            surfaceList,
            sharedCamera!!.createARSessionStateCallback(
                appSessionStateCallback, backgroundHandler
            ),
            backgroundHandler
        );
    }

    var cameraCapSession: CameraCaptureSession? = null
    val appSessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraCapSession = session
            //session.capture(requestBuilder!!.build(), appCapture, backgroundHandler)
            println("Configured!")
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            1 / 0
        }
    }

    val appCapture = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            println("===*===")
            for (k in result.physicalCameraResults.keys) {
                print(k)
                1/0
            }
            println("===*===")
        }

    }

}