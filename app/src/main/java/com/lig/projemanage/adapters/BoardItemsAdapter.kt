package com.lig.projemanage.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lig.projemanage.R
import com.lig.projemanage.models.Board
import kotlinx.android.synthetic.main.item_board.view.*
import kotlinx.android.synthetic.main.nav_header_main.*

open class BoardItemsAdapter(private val context: Context, private var list: ArrayList<Board>):
            RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var myOnClickListener: OnIClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(LayoutInflater.from(context)
           .inflate(R.layout.item_board, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.iv_board_image_recycle)

            holder.itemView.tv_name.text = model.name
            holder.itemView.tv_created_by.text = "Created by: ${model.createdBy}"
            holder.itemView.setOnClickListener {
                if(myOnClickListener != null){
                    myOnClickListener!!.onClick(position, model)
                }
            }
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }

    interface OnIClickListener{
        fun onClick(position: Int, model: Board)
    }

    fun setOnClickListener(onClickListener: OnIClickListener){
        this.myOnClickListener = onClickListener
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)


}