package at.sw21_tug.team_25.expirydates.ui.list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.misc.Util
import java.util.*

class ListFragment : Fragment() {
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // add other menu items in language_choice_menu / choose different menu to show here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.language_choice_menu, menu)
    }

    override fun onResume() {
        if ((this.activity as MainActivity).updateLayoutList.contains(R.id.navigation_list)) {
            (this.activity as MainActivity).updateLayoutList.remove(R.id.navigation_list)
            (this.activity as MainActivity).refreshCurrentFragment()
        }
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.language_en -> {
                val toast = Toast.makeText(activity, "English clicked", Toast.LENGTH_SHORT)
                toast.show()
                Util.setLanguage("en", requireActivity())
                Util.setLocale(requireActivity(), Locale("en"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }

            R.id.language_ru -> {
                val toast = Toast.makeText(activity, "Russian clicked", Toast.LENGTH_SHORT)
                toast.show()

                Util.setLanguage("ru", requireActivity())
                Util.setLocale(requireActivity(), Locale("ru"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }
        }
        (this.activity as MainActivity).requestUpdates(R.id.navigation_list)

        return false
    }

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
                itemsRecyclerView.adapter = ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
            }
        })
        return root
    }
}