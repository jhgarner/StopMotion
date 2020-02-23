/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.stopmotion

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
class HelloSceneformActivity : AppCompatActivity() {

    private var arFragment: ArFragmentGuide? = null
    private var andyRenderable: ModelRenderable? = null
    private var setupState = 0
    private var anchors = arrayListOf<Pose>()
    // CompletableFuture requires api level 24
// FutureReturnValueIgnored is not valid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_ux)
        Toast.makeText(
            this,
            "Click a surface in the middle of your scene.",
            Toast.LENGTH_LONG
        )
            .show()
        //arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragmentGuide?
        arFragment!!.messageBox = findViewById<TextView>(R.id.text_view)
        val session = arFragment!!.arSceneView.session
        // When you build a Renderable, Sceneform loads its resources in the background while returning
// a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
            .setSource(this, Uri.parse("andy.sfb"))
            .build()
            .thenAccept { renderable: ModelRenderable? ->
                andyRenderable = renderable
            }
            .exceptionally { throwable: Throwable? ->
                val toast =
                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                null
            }
        arFragment!!.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane?, motionEvent: MotionEvent? ->
            println("TAPPED")
            if (andyRenderable == null) {
                return@setOnTapArPlaneListener
            }
            if (setupState == 0) {
                arFragment!!.cameraPos = arFragment!!.arSceneView.arFrame!!.camera.pose
                // Create the Anchor.
                val anchor = hitResult.createAnchor()
                arFragment!!.scenePos = anchor.pose
                val cPos = arFragment!!.cameraPos!!.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
                val cam = arFragment!!.cameraPos

                //println("===========")
                //for (a in anchors) {
                    //val cPos = camera.pose.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
                    //val pos = a.transformPoint(floatArrayOf(0.0f, 0.0f, 0.0f))
                    //Log.w("test", (pos[0].toString() + " " + pos[1].toString() + " " + pos[2].toString()))
                    //Log.w("camera", cPos[0].toString() + " " + cPos[1].toString() + " " + cPos[2].toString())
                    //println( (pos[0].toString() + " " + pos[1].toString() + " " + pos[2].toString()))
                    //println(cPos[0].toString() + " " + cPos[1].toString() + " " + cPos[2].toString())
                    //println("----------")
                //}
                //println("===========")
                val anchorNode = AnchorNode()
                anchorNode.setWorldPosition(Vector3(cPos[0], cPos[1], cPos[2]))
                anchorNode.setWorldRotation(Quaternion(cam!!.qx(), cam!!.qy(), cam!!.qz(), cam!!.qw()))
                anchorNode.setParent(arFragment!!.arSceneView.scene)
                // Create the transformable andy and add it to the anchor.
                val andy =
                    TransformableNode(arFragment!!.transformationSystem)
                andy.setParent(anchorNode)
                andy.renderable = andyRenderable
                andy.select()

                //(findViewById<ImageButton>(R.id.imageButton))!!.visibility = View.VISIBLE
            }
        }

        //(findViewById<ImageButton>(R.id.imageButton))!!.setOnClickListener {
            //1 / 0
        //}


    }

    companion object {
        private val TAG = HelloSceneformActivity::class.java.simpleName
        private const val MIN_OPENGL_VERSION = 3.0
        /**
         * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
         * on this device.
         *
         *
         * Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
         *
         *
         * Finishes the activity if Sceneform can not run
         */
        fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
            if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
                Log.e(
                    TAG,
                    "Sceneform requires Android N or later"
                )
                Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
                return false
            }
            val openGlVersionString =
                (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                    .deviceConfigurationInfo
                    .glEsVersion
            if (openGlVersionString.toDouble() < MIN_OPENGL_VERSION) {
                Log.e(
                    TAG,
                    "Sceneform requires OpenGL ES 3.0 later"
                )
                Toast.makeText(
                    activity,
                    "Sceneform requires OpenGL ES 3.0 or later",
                    Toast.LENGTH_LONG
                )
                    .show()
                activity.finish()
                return false
            }
            return true
        }
    }
}