package com.oxad.fitox.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oxad.fitox.R
import com.oxad.fitox.model.TrainerModel

    class Admin_filter_adapter(
private var trainerList: List<TrainerModel>,
private var selectedId: Long = -1, // Default select "All" (id = -1)
private val onClick: (TrainerModel) -> Unit
) : RecyclerView.Adapter<Admin_filter_adapter.TrainerViewHolder>() {

    inner class TrainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trainerNameText: TextView = itemView.findViewById(R.id.adminNameText)
        val underline: View = itemView.findViewById(R.id.underlineView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin, parent, false)
        return TrainerViewHolder(view)
    }

        override fun onBindViewHolder(holder: TrainerViewHolder, position: Int) {
            val trainer = trainerList[position]
            holder.trainerNameText.text = trainer.name

            // Show underline + bold if selected
            if (trainer.coachid == selectedId) {
                holder.underline.visibility = View.VISIBLE
                holder.trainerNameText.setTypeface(null, Typeface.BOLD)
            } else {
                holder.underline.visibility = View.INVISIBLE
                holder.trainerNameText.setTypeface(null, Typeface.NORMAL)
            }

            holder.itemView.setOnClickListener {
                // Update selection
                val previousSelected = selectedId
                selectedId = trainer.coachid
                notifyItemChanged(trainerList.indexOfFirst { it.coachid == previousSelected }) // refresh old selected
                notifyItemChanged(position) // refresh new selected

                onClick(trainer)
            }
        }
        fun selectAll() {
            selectedId = -1 // "All" selected
            notifyDataSetChanged()
        }
    override fun getItemCount(): Int = trainerList.size
}