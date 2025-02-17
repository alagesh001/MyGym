import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oxad.mygym.R
import com.oxad.mygym.model.LoginResult

class GymselectAdapter(gmyListIs: List<LoginResult>,private val listener: OnItemSelectedListener) :
    RecyclerView.Adapter<GymselectAdapter.GymListViewHolder>() {
    private val gymList: List<LoginResult> = gmyListIs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_gym, parent, false)
        return GymListViewHolder(view)
    }

    override fun onBindViewHolder(holder: GymListViewHolder, position: Int) {
        val gymListItem: LoginResult = gymList[position]
        holder.gymName.text = gymListItem.GymName
        holder.gymLoc.text = gymListItem.Location
        holder.gymmembCount.text = gymListItem.membercount.toString()

        holder.item_ll.setOnClickListener {
            listener.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return gymList.size
    }

    class GymListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_ll: RelativeLayout = itemView.findViewById(R.id.item_ll)
        var gymName: TextView = itemView.findViewById<TextView>(R.id.gymname_tv)
        var gymLoc: TextView = itemView.findViewById<TextView>(R.id.gymloc_tv)
        var gymmembCount: TextView = itemView.findViewById<TextView>(R.id.memberCount)

    }
    interface OnItemSelectedListener {

        fun onItemSelected(item: Int)

    }
}