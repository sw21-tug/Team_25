package at.sw21_tug.team_25.expirydates.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentActivity
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.ui.detailview.ui.DetailViewActivity

class ExpItemRecyclerViewAdapter(private val activity: FragmentActivity, private val expItems: List<ExpItem>):
    RecyclerView.Adapter<ExpItemRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expItem = expItems[position]
        val expItemText = expItem.name.capitalize() + "  " + expItem.date
        holder.textView.text = expItemText
        holder.textView.setOnClickListener {
            DetailViewActivity.openDetailView(activity , expItem)
        }
    }

    override fun getItemCount(): Int {
        val expItems = expItems
        return expItems.size
    }

}