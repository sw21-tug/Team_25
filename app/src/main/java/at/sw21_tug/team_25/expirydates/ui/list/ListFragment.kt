package at.sw21_tug.team_25.expirydates.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import at.sw21_tug.team_25.expirydates.utils.Util
import at.sw21_tug.team_25.expirydates.utils.Util.Companion.hideKeyboard
import at.sw21_tug.team_25.expirydates.ui.settings.SettingsView
import java.util.*

class ListFragment : Fragment() {
    private lateinit var listViewModel: ListViewModel
    private lateinit var itemsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // add other menu items in language_choice_menu / choose different menu to show here
        inflater.inflate(R.menu.language_choice_menu, menu)
        inflater.inflate(R.menu.sort_by_name_menu, menu)
        inflater.inflate(R.menu.sort_by_date_menu, menu)
        inflater.inflate(R.menu.settings, menu)
    }


    override fun onResume() {
        if ((this.activity as MainActivity).updateLayoutList.contains(R.id.navigation_list)) {
            (this.activity as MainActivity).updateLayoutList.remove(R.id.navigation_list)
            (this.activity as MainActivity).refreshCurrentFragment()
        }
        hideKeyboard(this.activity as MainActivity, itemsRecyclerView)
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_menu -> {
                SettingsView.openSettingsView(this.activity as MainActivity)
            }
        }

        /*
        when (item.itemId) {
            R.id.language_en -> {
                Util.setLanguage("en", requireActivity())
                Util.setLocale(requireActivity(), Locale("en"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }

            R.id.language_ru -> {
                Util.setLanguage("ru", requireActivity())
                Util.setLocale(requireActivity(), Locale("ru"))

                (this.activity as MainActivity).refreshCurrentFragment()
            }

            R.id.sort_name_asc -> {
                listViewModel.expItems =
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                        .readAllItemsSorted(1, "name")
                listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        this.itemsRecyclerView.adapter =
                            ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
                    }
                })
            }
            R.id.sort_name_desc -> {
                listViewModel.expItems =
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                        .readAllItemsSorted(0, "name")
                listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        this.itemsRecyclerView.adapter =
                            ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
                    }
                })
            }
            R.id.sort_exp_asc -> {
                listViewModel.expItems =
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                        .readAllItemsSorted(1, "date")
                listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        this.itemsRecyclerView.adapter =
                            ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
                    }
                })
            }
            R.id.sort_exp_desc -> {
                listViewModel.expItems =
                    ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                        .readAllItemsSorted(0, "date")
                listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        this.itemsRecyclerView.adapter =
                            ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
                    }
                })
            }

        }
        (this.activity as MainActivity).requestUpdates(R.id.navigation_list)*/

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

        this.itemsRecyclerView = root.findViewById<RecyclerView>(R.id.items_rv)
        this.itemsRecyclerView.layoutManager = LinearLayoutManager((activity as MainActivity))

        listViewModel.expItems =
            ExpItemDatabase.getDatabase((activity as MainActivity)).expItemDao()
                .readAllItemsSorted(1, "date")
        listViewModel.expItems?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.itemsRecyclerView.adapter =
                    ExpItemRecyclerViewAdapter((this.activity as FragmentActivity), it)
            }
        })
        return root
    }
}
