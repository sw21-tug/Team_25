package at.sw21_tug.team_25.expirydates

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.sw21_tug.team_25.expirydates.misc.Util
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var navView: BottomNavigationView
    lateinit var navMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = Util.getLanguage(this)
        Util.setLocale(this, language)

        // load language test
//        val language = "ru"
//        val locale = Locale(language)
//        val configuration = resources.configuration
//        configuration.setLocale(locale)
//
//        val newConfig = Configuration()
//        newConfig.setLocale(locale)
        //baseContext.applicationContext.createConfigurationContext(newConfig)
        //baseContext.resources.displayMetrics.setTo(baseContext.resources.displayMetrics)
        //baseContext.resources.updateConfiguration(newConfig, baseContext.resources.displayMetrics)

        setContentView(R.layout.activity_main)


        navView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_list
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navMenu = navView.menu
    }

    fun refreshCurrentFragment(){
        val id = navController.currentDestination?.id

        val home = navMenu.findItem(R.id.navigation_home)
        if (home != null) {
            home.title = getString(R.string.title_home)
        }
        val add = navMenu.findItem(R.id.navigation_add)
        if (add != null) {
            add.title = getString(R.string.title_add)
        }
        val list = navMenu.findItem(R.id.navigation_list)
        if (list != null) {
            list.title = getString(R.string.title_list)
        }
        updateTitle()

        navController.popBackStack(id!!,true)
        navController.navigate(id)
    }

    fun updateTitle() {
        Log.i("test", "" + navController.currentDestination?.label)
        when (navController.currentDestination?.label) {
            "Добавлять" -> navController.currentDestination?.label = getString(R.string.title_add)
            "Add" -> navController.currentDestination?.label = getString(R.string.title_add)

            "Заставке" -> navController.currentDestination?.label = getString(R.string.title_home)
            "Home" -> navController.currentDestination?.label = getString(R.string.title_home)

            "Список" -> navController.currentDestination?.label = getString(R.string.title_list)
            "List" -> navController.currentDestination?.label = getString(R.string.title_list)
        }
    }
}