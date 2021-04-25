package at.sw21_tug.team_25.expirydates

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.sw21_tug.team_25.expirydates.ui.detailview.ui.DetailViewActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_list))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun openDetailView(view: View) {

        val bundle = Bundle();
        val names = arrayOf("Milk", "Eggs", "Bread");

        val time = System.currentTimeMillis()

        bundle.putString("name", names[Random.nextInt(3)])
        bundle.putLong("date", Random.nextLong(time, time + 1000000))

        val intent = Intent(this, DetailViewActivity::class.java);
        intent.putExtras(bundle);
        startActivity(intent)
    }
}