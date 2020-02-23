package com.example.stopmotion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.Timer
import kotlin.concurrent.schedule

import kotlinx.android.synthetic.main.player_activity.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.CountDownTimer
import kotlin.concurrent.timer


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        // TODO need to get a File dir that is a path to the directory
        // where the images are kept
        //createFileArray(dir)

        // TODO make this run the functions to play the file
        val phot1 = R.drawable.test
        val phot2 = R.drawable.sw2
        val phot3 = R.drawable.sw1

        val arr: ArrayList<Int> = ArrayList()
        arr.add(phot1)
        arr.add(phot2)
        arr.add(phot3)

        playBtn.setOnClickListener {

            playFileArray(arr)

        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        infoBtn.setOnClickListener {


        }

    }


    fun createFileArray(dir: File): ArrayList<File> {

        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = dir.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".jpeg")) {
                    fileList.add(currentFile.absoluteFile)
                }
            }
        }
        return fileList
    }

    fun playFileArray(fileArr: ArrayList<Int>) {

        var image: Int = 0
        val imageView: ImageView = findViewById(R.id.imageView)
        val millsInFuture: Long = ((fileArr.size - 1) * 1000).toLong()


            object : CountDownTimer(millsInFuture, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    imageView.setImageResource(fileArr[image])
                    image++
                }

                override fun onFinish() {
                    imageView.setImageResource(fileArr[image])
                }
            }.start()

        }

}





