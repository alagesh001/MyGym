package com.oxad.fitox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oxad.fitox.model.Member

class MemberSearchAdapter : RecyclerView.Adapter<MemberSearchAdapter.MemberViewHolder>() {

    private var members: List<Member> = emptyList()

    fun submitList(newList: List<Member>) {
        members = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount(): Int = members.size

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(member: Member) {
            (itemView as TextView).text = member.name
        }
    }
}
