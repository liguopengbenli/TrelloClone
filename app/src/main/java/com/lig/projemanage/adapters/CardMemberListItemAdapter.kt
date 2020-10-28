package com.lig.projemanage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lig.projemanage.R
import com.lig.projemanage.models.SelectedMembers
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.item_card_selected_member.view.*

open class CardMemberListItemAdapter(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private var onClickListener: MyonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
            R.layout.item_card_selected_member,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            if(position == list.size -1){ // last postion display +
                   holder.itemView.iv_add_member.visibility = View.VISIBLE
                   holder.itemView.iv_selected_member_image.visibility = View.GONE
            }else{ // others display member img
                holder.itemView.iv_add_member.visibility = View.GONE
                holder.itemView.iv_selected_member_image.visibility = View.VISIBLE
                Glide // using glide to get media from its url
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.itemView.iv_selected_member_image)
            }
            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick()
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun setMyOnclickListener(myOnclickListener : MyonClickListener){
        onClickListener = myOnclickListener
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    /**
     * An interface for onclick items.
     */
    interface MyonClickListener {
        fun onClick()
    }


}