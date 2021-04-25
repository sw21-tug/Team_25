package at.sw21_tug.team_25.expirydates.ui.detailview.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import at.sw21_tug.team_25.expirydates.R
import java.util.*

class DetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_detail_view)

        val bundle = intent.extras;

        if(bundle != null) {

            val name = bundle.getString("name").orEmpty();
            val date_int = bundle.getLong("date");

            val date = Date(date_int);

            OpenPopup(name, date);

        }
    }

    fun OpenPopup(name : String, date : Date)
    {

        val product_name = findViewById<TextView>(R.id.product_name);
        product_name.text = name;

        val exp_date = findViewById<TextView>(R.id.exp_date);
        exp_date.text = date.toString()
    }

}