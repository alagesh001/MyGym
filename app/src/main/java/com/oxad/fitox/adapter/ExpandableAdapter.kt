package com.oxad.fitox.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.oxad.fitox.R

import com.oxad.fitox.model.TrainerModel

class  ExpandableAdapter( private val context: Context,private val listener:TrainerListener ) :
    RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {
    private val itemList = mutableListOf<TrainerModel>()

    fun submitList(newList: List<TrainerModel>,) {
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAdminId: TextView = itemView.findViewById(R.id.tvadminid)

        val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        val expandableLayout: View = itemView.findViewById(R.id.expandableLayout)
        val ivProfile: ImageView = itemView.findViewById(R.id.imgProfile)
        val card_item: View = itemView.findViewById(R.id.item_viewcard)
        val update_tv: View = itemView.findViewById(R.id.update_tv)
        val relative_lay: RelativeLayout = itemView.findViewById(R.id.relative_lay)



    }
interface TrainerListener{
    fun onUpdate(trainerModel: TrainerModel)
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trainer_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]


        if(item.role != "dietician"){
            holder.tvTitle.text = item.name
            holder.tvDetails.text = item.mob
            holder.tvAdminId.text = item.coachid.toString()
           // holder.relative_lay.setBackgroundColor(ContextCompat.getColor(context, R.color.secondary_color))
        }


        /*Glide.with(context)
            .load("https://greenyellow-octopus-821386.hostingersite.com/images/gympicsample.jpg")
            .placeholder(R.drawable.ic_launcher_foreground) // Default image
            .transition(DrawableTransitionOptions.withCrossFade(400))
            .circleCrop() // Make image round
            .into(holder.ivProfile)
*/

        // Set visibility based on expansion state
        holder.expandableLayout.visibility = if (item.isExpanded) View.VISIBLE else View.GONE

        // Handle click to expand/collapse
        holder.card_item.setOnClickListener {
            item.isExpanded = !item.isExpanded
            notifyItemChanged(position) // Refresh item view
        }
        holder.update_tv.setOnClickListener {
           listener.onUpdate(item)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
