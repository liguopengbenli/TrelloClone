package com.lig.projemanage.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lig.projemanage.R
import com.lig.projemanage.activities.TaskListActivity
import com.lig.projemanage.models.Card
import com.lig.projemanage.models.SelectedMembers
import kotlinx.android.synthetic.main.item_card.view.*

open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: MyonClickListener? = null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val model = list[position]

        if(holder is MyViewHolder){
            if(model.labelColor.isNotEmpty()){
                holder.itemView.view_label_color.visibility = View.VISIBLE
                holder.itemView.view_label_color.setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.itemView.view_label_color.visibility = View.GONE
            }

            holder.itemView.tv_card_name.text = model.name

            if((context as TaskListActivity).mAssignedMemberDetailsList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for(i in context.mAssignedMemberDetailsList.indices){
                    for(j in model.assignedTo){
                        if(context.mAssignedMemberDetailsList[i].id==j){
                            val selectedMembers = SelectedMembers(context.mAssignedMemberDetailsList[i].id, context.mAssignedMemberDetailsList[i].image)
                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }
                if(selectedMembersList.size > 0){ // if only one owner don't display member
                    if(selectedMembersList.size == 1 && selectedMembersList[0].id == model.createdBy){
                        holder.itemView.rv_card_selected_members_list.visibility = View.GONE
                    }else{
                        holder.itemView.rv_card_selected_members_list.visibility = View.VISIBLE
                        holder.itemView.rv_card_selected_members_list.layoutManager = GridLayoutManager(context, 4)
                        val adapter = CardMemberListItemAdapter(context, selectedMembersList, false)
                        holder.itemView.rv_card_selected_members_list.adapter = adapter

                        adapter.setMyOnclickListener(object: CardMemberListItemAdapter.MyonClickListener{
                            override fun onClick() {
                                if(onClickListener != null){
                                    onClickListener!!.onClick(position)
                                }
                            }

                        })
                    }
                }else{
                    holder.itemView.rv_card_selected_members_list.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position)
                }
            }

        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */
    fun setOnClickListener(onClickListener: MyonClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * An interface for onclick items.
     */
    interface MyonClickListener {
        fun onClick(cardPosition: Int)
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}