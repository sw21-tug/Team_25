package at.sw21_tug.team_25.expirydates.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase

class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        listViewModel =
                ViewModelProvider(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val itemsRecyclerView = root.findViewById<RecyclerView>(R.id.items_rv)
        itemsRecyclerView.layoutManager = LinearLayoutManager((activity as MainActivity))

        listViewModel.expItems = ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao().readAllItems()
        listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                itemsRecyclerView.adapter = ExpItemRecyclerViewAdapter(it)
            }
        })
        return root
    }
}