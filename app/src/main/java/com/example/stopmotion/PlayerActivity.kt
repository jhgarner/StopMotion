package com.example.stopmotion

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.concurrent.schedule

import kotlinx.android.synthetic.main.player_activity.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import androidx.core.os.postDelayed
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        // TODO need to get a File dir that is a path to the directory
        // where the images are kept
        val file: String = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + "/Project1/"
        val dir = File(file)
        val arr: ArrayList<File> = createFileArray(dir)
        println(file)


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
                    fileList.add(currentFile.absoluteFile)
            }
        }
        return fileList
    }

    fun playFileArray(fileArr: ArrayList<File>) {

        fileArr.sortBy {
            a -> a.name.toInt()
        }
        val imageView: ImageView = findViewById(R.id.imageView)
        val countInterval: Long = (1200 - (seekBar.progress * 100 + 100)).toLong()

        var counter: Int = -1

        fun update(){

            runOnUiThread(Runnable { (imageView.setImageURI(Uri.fromFile(fileArr[counter])))})
            counter++
        }

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                update()
                if(counter >= fileArr.size - 1){
                    timer.cancel()
                    timer.purge()
                }
            }
        } , 0, countInterval)


    }
}





