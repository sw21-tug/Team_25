package at.sw21_tug.team_25.expirydates

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
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


        val inflater:LayoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        var popup_view = inflater.inflate(R.layout.fragment_detail_view, null);

        var popup_window = PopupWindow(
            popup_view,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popup_window.elevation = 10.0F

        popup_window.showAtLocation(view, Gravity.CENTER, 0, 0)


    }
}