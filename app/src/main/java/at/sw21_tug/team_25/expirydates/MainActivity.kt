package at.sw21_tug.team_25.expirydates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.sw21_tug.team_25.expirydates.data.ExpItem
import at.sw21_tug.team_25.expirydates.data.ExpItemDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivity : AppCompatActivity() {

    var db: ExpItemDatabase?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_list
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        this.db = ExpItemDatabase.getDatabase(this.applicationContext)
        createFakeData() // TODO: Remove this once tests is implemented
    }

    /*
    * Remove me this once tests are implemented
    * */
    fun createFakeData() {
        var expItemDao = this.db?.expItemDao()
        //expItemDao?.deleteAllItems()
        GlobalScope.async {
            expItemDao?.deleteAllItems()
        }

        for (i in 10..30) {
            GlobalScope.async {
                // yyyy-MM-dd hh:mm:ss
                var date = "20" + i.toString()
                date = date + "-05-01 20:43:45"
                var expItem = ExpItem("salami " + i.toString(), date)
                expItemDao?.insertItem(expItem)
            }
        }
    }


//    inner class Listener : NavController.OnDestinationChangedListener {
//        override fun onDestinationChanged(
//            controller: NavController,
//            destination: NavDestination,
//            arguments: Bundle?
//        ) {
//            if(destination.label == "List") {
//                this@MainActivity.db?.expItemDao()?.readAllItems()
//
//            }
//        }
//
//    }
}
