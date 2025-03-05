package com.oxad.fitox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oxad.fitox.R
import com.oxad.fitox.model.Member


class RenewalAdapter(private val clientList: List<Member>) : RecyclerView.Adapter<RenewalAdapter.RenewalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RenewalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_renewal, parent, false)
        return RenewalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RenewalViewHolder, position: Int) {
        val client = clientList[position]
        holder.numberTextView.text = (position + 1).toString() // Incrementing number
        holder.nameTextView.text = client.name
        holder.mobileTextView.text = client.mobile.toString()
        holder.renewalDateTextView.text = client.renewal_date
    }

    override fun getItemCount(): Int {
        return clientList.size
    }

    class RenewalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val mobileTextView: TextView = itemView.findViewById(R.id.mobileTextView)
        val renewalDateTextView: TextView = itemView.findViewById(R.id.renewalDateTextView)
    }
}

