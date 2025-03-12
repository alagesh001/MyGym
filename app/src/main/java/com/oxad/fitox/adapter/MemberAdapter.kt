package com.oxad.fitox.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oxad.fitox.R
import com.oxad.fitox.model.Member
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MemberAdapter(private val context: Context, private val listener: MemberAdapterListener) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
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
        if(isDateExpired(member.renewal_date)){
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
        }else if(isDateCloseToExpiry(member.renewal_date)){
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.yellow)
        }else{
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green)
        }

        if(member.due_amount!! >0){
    holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tertiary_color)
holder.dueTextView.visibility = VISIBLE
}else{
            holder.dueTextView.visibility = GONE

        }
        holder.rootLlView.setOnClickListener {
            member.isExpanded = !member.isExpanded
            notifyItemChanged(position) // Refresh item view
        }
        holder.updateTextView.setOnClickListener { member.isExpanded = !member.isExpanded
            notifyItemChanged(position)
            listener.onUpdateClick(member,false)
        }
        holder.dueTextView.setOnClickListener { member.isExpanded = !member.isExpanded
            notifyItemChanged(position)
            listener.onDueClick(member)
        }
        holder.renewTextView.setOnClickListener { member.isExpanded = !member.isExpanded
            notifyItemChanged(position)
            listener.onUpdateClick(member,true)
        }
        holder.sendwtsapp_tv.setOnClickListener {
            listener.onWhatsappSend(member)
        }
    }

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootLlView: LinearLayout = view.findViewById(R.id.rootView)
        val selectView: LinearLayout = view.findViewById(R.id.card_bg)

        private val nameTextView: TextView = view.findViewById(R.id.tvName)
        private val mobileTextView: TextView = view.findViewById(R.id.tvMobile)
        private val goalTextView: TextView = view.findViewById(R.id.tvGoal)

        private val renewalDateTextView: TextView = view.findViewById(R.id.tvRenewalDate)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        val expandableLayout: View = itemView.findViewById(R.id.expandableLayout)
        val updateTextView: TextView = itemView.findViewById(R.id.update_tv)
        val renewTextView: TextView = itemView.findViewById(R.id.renew_tv)
        val sendwtsapp_tv: TextView = itemView.findViewById(R.id.sendwtsapp_tv)
        val dueTextView: TextView = itemView.findViewById(R.id.due_tv)

        fun bind(member: Member,position: Int) {
            numberTextView.text = (position + 1).toString() // Incrementing number
            nameTextView.text = member.name
            mobileTextView.text = member.mobile.toString()
            goalTextView.text =  when(member.goal){
                1 -> "Weight Loss"
                2 -> "Weight Gain"
                3 -> "Muscle Tone"
                4 -> "Others"
                else -> {"Others"}
            }

            renewalDateTextView.text = "${member.renewal_date}"
            expandableLayout.visibility = if (member.isExpanded) View.VISIBLE else View.GONE

        }
    }

    fun isDateExpired(dateString: String, format: String = "yyyy-MM-dd"): Boolean {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())

        return try {
            val givenDate = dateFormat.parse(dateString) // Convert string to Date
            val currentDate = Calendar.getInstance().time // Get current date

            givenDate?.before(currentDate) ?: false // Check if given date is before today
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false if parsing fails
        }
    }

    fun isDateCloseToExpiry(dateString: String, format: String = "yyyy-MM-dd", daysBeforeExpiry: Int = 5): Boolean {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())

        return try {
            val givenDate = dateFormat.parse(dateString) // Convert string to Date
            val currentDate = Calendar.getInstance() // Get current date
            val expiryThreshold = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, daysBeforeExpiry) }

            givenDate?.before(expiryThreshold.time) == true && givenDate.after(currentDate.time)
        } catch (e: Exception) {
            e.printStackTrace()
            false // Return false if parsing fails
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
        fun onUpdateClick(member:Member,isRenew:Boolean)
        fun onDueClick(member:Member)

        fun onWhatsappSend(member:Member)

    }
}
