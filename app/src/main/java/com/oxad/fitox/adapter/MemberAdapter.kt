package com.oxad.fitox.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.oxad.fitox.R
import com.oxad.fitox.model.Member
import com.oxad.fitox.utils.Utils
import com.oxad.fitox.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MemberAdapter(private val context: Context, private val listener: MemberAdapterListener,private val isActive: Int) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }
    private val members = mutableListOf<Member>()

    fun submitList(newList: List<Member>,) {
        members.clear()
       // Log.e("chadksaa","cachedMembers adfsasdf "+ Gson().toJson(newList))

        members.addAll(newList)

        notifyDataSetChanged()
    }
    fun submitListAdd(newList: List<Member>,) {
        // Log.e("chadksaa","cachedMembers adfsasdf "+ Gson().toJson(newList))

        members.addAll(newList)

        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        var typeOfMember = 1
        //1 Active
        //2 Close to expiry
        //3 Expired
        //4 Due to be paid
        member.let {
            holder.bind(it,position)
        }
        if(isDateExpired(member.renewal_date)){
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.red)
            typeOfMember = 3
        }else if(isDateCloseToExpiry(member.renewal_date)){
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.yellow)
            typeOfMember = 2
        }else{
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.green_list)
            typeOfMember = 1
        }

        if(member.due_amount!! >0){
            holder.selectView.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tertiary_color)
            typeOfMember = 4
            holder.dueTextView.visibility = VISIBLE
        }else{
            holder.dueTextView.visibility = GONE

        }
        if(isActive == 3){
            holder.dietscheduletv.visibility = VISIBLE
            holder.sendwtsapp_tv.visibility = VISIBLE

        }else if(isActive == 444){
            holder.dietscheduletv.visibility = GONE
            holder.sendwtsapp_tv.visibility = GONE
            holder.renewTextView.visibility = GONE
            holder.updateTextView.visibility = GONE
            holder.markAttend_tv.visibility = GONE
            holder.dueTextView.visibility = GONE


        }else
        {
            holder.dietscheduletv.visibility = GONE
            holder.sendwtsapp_tv.visibility = GONE
        }
        holder.ivCopyuid.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("User ID", holder.uidTextView.text.toString())
            clipboard.setPrimaryClip(clip)
            Utils.showToast(context, "Copied: ${ holder.uidTextView.text}")
        }
        holder.ivCopyumob.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("User ID",  holder.mobileTextView.text.toString())
            clipboard.setPrimaryClip(clip)
            Utils.showToast(context, "Copied: ${ holder.mobileTextView.text}")

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
        holder.dietscheduletv.setOnClickListener { member.isExpanded = !member.isExpanded
            notifyItemChanged(position)
            listener.onDietSchedule(member.userid)
        }

        holder.sendwtsapp_tv.setOnClickListener {
            listener.onWhatsappSend(member)
        }
        holder.callbtn_iv.setOnClickListener {
            listener.oncallClick(member.mobile)
        }
        holder.wtsappbtn_iv.setOnClickListener {
            listener.onWhatsappClick(member.mobile,member.name,member.renewal_date,member.due_amount,typeOfMember)
        }
        holder.markAttend_tv.setOnClickListener {
            listener.onMarkAttendClick(member.userid)
        }
    }

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootLlView: LinearLayout = view.findViewById(R.id.rootView)
        val selectView: LinearLayout = view.findViewById(R.id.card_bg)
        val uidTextView: TextView = view.findViewById(R.id.tvUserid)
         val ivCopyuid = view.findViewById<ImageView>(R.id.ivCopyuid)
         val ivCopyumob = view.findViewById<ImageView>(R.id.ivCopyumob)

        private val nameTextView: TextView = view.findViewById(R.id.tvName)
        val mobileTextView: TextView = view.findViewById(R.id.tvMobile)
        private val goalTextView: TextView = view.findViewById(R.id.tvGoal)
        private val tvAttendCount: TextView = view.findViewById(R.id.tvAttendCount)
        private val tvlastAttend: TextView = view.findViewById(R.id.tvlastAttend)

        val dietscheduletv: TextView = view.findViewById(R.id.diet_schedule_tv)

        private val renewalDateTextView: TextView = view.findViewById(R.id.tvRenewalDate)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        val expandableLayout: View = itemView.findViewById(R.id.expandableLayout)
        val updateTextView: TextView = itemView.findViewById(R.id.update_tv)
        val renewTextView: TextView = itemView.findViewById(R.id.renew_tv)
        val sendwtsapp_tv: TextView = itemView.findViewById(R.id.sendwtsapp_tv)
        val dueTextView: TextView = itemView.findViewById(R.id.due_tv)
        val callbtn_iv: ImageView = itemView.findViewById(R.id.call_btn)
        val wtsappbtn_iv: ImageView = itemView.findViewById(R.id.wtapp_btn)
        val markAttend_tv: TextView = itemView.findViewById(R.id.markAttend_tv)
        val joindate_tv: TextView = itemView.findViewById(R.id.tvjoinDate)

        fun bind(member: Member,position: Int) {
            uidTextView.text = member.userid.toString() // Incrementing number
            numberTextView.text = (position + 1).toString() // Incrementing number
            nameTextView.text = member.name
            mobileTextView.text = member.mobile.toString()
            tvAttendCount.text = "Attendance - ${member.attendance_count}"
            tvlastAttend.text = "Last marked ${member.last_attend}"
            joindate_tv.text = "Join Date ${member.joiningdate ?: "NA"}"

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
        fun onDietSchedule(userid:Long)
        fun onWhatsappClick(mobile:Long,userName: String, expiryDate: String, dueAmount: Int,type:Int)
        fun oncallClick(mobile:Long)
        fun onMarkAttendClick(userid:Long)


    }
}
