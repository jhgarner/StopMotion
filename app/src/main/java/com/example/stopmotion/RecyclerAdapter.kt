package com.example.stopmotion

import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.editor_images_list.view.*

class RecyclerAdapter(private val photos: ArrayList<Int>) : RecyclerView.Adapter<RecyclerAdapter.PhotoHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.PhotoHolder {
        //To change body of created functions use File | Settings | File Templates.
        val inflatedView = parent.inflate(R.layout.editor_images_list, false)
        return PhotoHolder(inflatedView)
    }

    override fun getItemCount() = photos.size

    override fun onBindViewHolder(holder: RecyclerAdapter.PhotoHolder, position: Int) {
        val itemPhoto = photos[position]
        holder.bindPhoto(itemPhoto)
    }

    //1
    class PhotoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        private var view: View = v
        private var photo: Int? = null

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            //val context = itemView.context
            //val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            //showPhotoIntent.putExtra(PHOTO_KEY, photo)
            //context.startActivity(showPhotoIntent)
        }

        fun bindPhoto(photo: Int) {
            view.image.setImageResource(photo)
            //Picasso.with(view.context).load(photo.url).into(view.itemImage)
            //view.itemDate.text = photo.humanDate
            //view.itemDescription.text = photo.explanation
        }


        companion object {
            //5
            private val PHOTO_KEY = "PHOTO"
        }
    }

}