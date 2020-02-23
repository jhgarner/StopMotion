package com.example.stopmotion

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    //private lateinit var recyclerView: RecyclerView
    /*private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_viewer)


        linearLayoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.images_display).layoutManager = linearLayoutManager

        var photosList: ArrayList<Int> = ArrayList<Int>()
        val phot1 = R.drawable.ap_creative_stock_header
        val phot2 = R.drawable.f81
        photosList.add(phot1)
        photosList.add(phot2)
        adapter = RecyclerAdapter(photosList)
        findViewById<RecyclerView>(R.id.images_display).adapter = adapter

        var helper : ItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

            override fun onMove(@NonNull recyclerView : RecyclerView, @NonNull dragged : RecyclerView.ViewHolder, @NonNull target : RecyclerView.ViewHolder): Boolean {

                var position_dragged : Int = dragged.getAdapterPosition()
                var position_target : Int = target.getAdapterPosition()

                Collections.swap(photosList, position_dragged, position_target)

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
        saved_confirmation.visibility = View.VISIBLE;
    }

}
