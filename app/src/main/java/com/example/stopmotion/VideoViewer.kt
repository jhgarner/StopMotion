package com.example.stopmotion

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_video_viewer.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(private val myDataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.editor_images_list, parent, false) as TextView

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

}


class VideoViewer : Activity() {

    private lateinit var adapter: RecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var finalList: ArrayList<File>
    //private lateinit var recyclerView: RecyclerView
    /*private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_viewer)


        linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.images_display).layoutManager = linearLayoutManager

        val path: String =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + "/Project1"

        val directory: File = File(path)

        val photosList: Array<File> = directory.listFiles()
        photosList.sortBy {
            a -> a.name.toInt()
        }
        finalList = ArrayList<File>()

        for(photo in photosList) {
            finalList.add(photo)
        }
        //var photosList: ArrayList<Int> = ArrayList<Int>()
        adapter = RecyclerAdapter(finalList)
        findViewById<RecyclerView>(R.id.images_display).adapter = adapter

        var helper : ItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(@NonNull recyclerView : RecyclerView, @NonNull dragged : RecyclerView.ViewHolder, @NonNull target : RecyclerView.ViewHolder): Boolean {

                var position_dragged : Int = dragged.getAdapterPosition()
                var position_target : Int = target.getAdapterPosition()

                Collections.swap(finalList, position_dragged, position_target)

                adapter.notifyItemMoved(position_dragged,position_target)

                return false
            }

            override fun onSwiped(@NonNull viewHolder : RecyclerView.ViewHolder, i: Int){

            }
        })
        helper.attachToRecyclerView(findViewById<RecyclerView>(R.id.images_display))
        //viewManager = LinearLayoutManager(this)
        //viewAdapter = MyAdapter(arrayOf("One", "Two", "Three"))

        /*recyclerView = findViewById<RecyclerView>(R.id.images_display).apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter
        }*/


    }

    fun saveLayout(view: View) {
        var i = 1;
        var success = true
        val path: String =
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + "/Project1"
        val directory: File = File(path)
        for (image in finalList) {
                var oldF: File = File(directory, image.name)
                var newF: File = File(directory, "p" + i.toString())
                if (!oldF.renameTo(newF)) {
                    success = false
                }
            i++
        }

        for (i in 1..finalList.size) {
            var oldF: File = File(directory, "p" + i)
            var newF: File = File(directory, i.toString())
            if (!oldF.renameTo(newF)) {
                success = false
            }
        }

        if(success) {
            saved_confirmation.visibility = View.VISIBLE;
        }
    }

}
