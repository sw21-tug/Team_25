package at.sw21_tug.team_25.expirydates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.sw21_tug.team_25.expirydates.misc.Util
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
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
    }
}