package com.oxad.mygym.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oxad.mygym.R
import com.oxad.mygym.model.Member

class MemberAdapter(private val listener: MemberAdapterListener) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }
    private val members = mutableListOf<Member>()

    fun submitList(newList: List<Member>,) {
        members.clear()
        members.addAll(newList)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        member.let {
            holder.bind(it,position)
        }
        holder.rootLlView.setOnClickListener {
            member.isExpanded = !member.isExpanded
            notifyItemChanged(position) // Refresh item view
        }
        holder.updateTextView.setOnClickListener {
            listener.onUpdateClick(member)
        }
    }

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootLlView: LinearLayout = view.findViewById(R.id.rootView)

        private val nameTextView: TextView = view.findViewById(R.id.tvName)
        private val mobileTextView: TextView = view.findViewById(R.id.tvMobile)
        private val renewalDateTextView: TextView = view.findViewById(R.id.tvRenewalDate)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        val expandableLayout: View = itemView.findViewById(R.id.expandableLayout)
        val updateTextView: TextView = itemView.findViewById(R.id.update_tv)

        fun bind(member: Member,position: Int) {
            numberTextView.text = (position + 1).toString() // Incrementing number
            nameTextView.text = member.name
            mobileTextView.text = member.mobile.toString()
            renewalDateTextView.text = "${member.renewal_date}"
            expandableLayout.visibility = if (member.isExpanded) View.VISIBLE else View.GONE

        }
    }

    class MemberDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.mobile == newItem.mobile
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
    interface MemberAdapterListener{
        fun onUpdateClick(member:Member)
    }
}
