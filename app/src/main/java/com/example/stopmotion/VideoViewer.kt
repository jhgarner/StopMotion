package com.example.stopmotion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_video_viewer.*

class VideoViewer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_viewer)


    }

    fun saveLayout(view: View) {
        saved_confirmation.visibility = View.VISIBLE;
    }

}
