package com.lig.projemanage.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lig.projemanage.R
import kotlinx.android.synthetic.main.item_label_color.view.*

class LabelColorListItemsAdapter(
    private val context: Context,
    private val list: ArrayList<String>,
    private val mSelectedColor: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_label_color, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if(holder is MyViewHolder){
            holder.itemView.view_main.setBackgroundColor(Color.parseColor(item))
            // by defaut all colors are visible and when selected only one is visible
            if(item == mSelectedColor){
                holder.itemView.iv_selected_color.visibility = View.VISIBLE
            }else {
                holder.itemView.iv_selected_color.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onItemClickListener != null){
                    onItemClickListener!!.onClick(position, item)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener{
        fun onClick(position: Int, color: String)
    }


}