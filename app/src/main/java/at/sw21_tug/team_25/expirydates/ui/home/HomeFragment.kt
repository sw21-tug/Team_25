package at.sw21_tug.team_25.expirydates.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.sw21_tug.team_25.expirydates.MainActivity
import at.sw21_tug.team_25.expirydates.R
import at.sw21_tug.team_25.expirydates.misc.Util
import at.sw21_tug.team_25.expirydates.utils.FoodSharingAPIClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private val requestLocation = 1
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var foodSharingApiClient: FoodSharingAPIClient

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // add other menu items in language_choice_menu / choose different menu to show here
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.language_choice_menu, menu)
    }

    override fun onResume() {
        if ((this.activity as MainActivity).updateLayoutList.contains(R.id.navigation_home)) {
            (this.activity as MainActivity).updateLayoutList.remove(R.id.navigation_home)
            (this.activity as MainActivity).refreshCurrentFragment()
        }
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
        }
        (this.activity as MainActivity).requestUpdates(R.id.navigation_home)
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestLocation)

        return root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        this.locationManager =
            (activity as MainActivity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val myLatitude: Double
        val myLongitude: Double
        val location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            myLatitude = 47.05875826931372
            myLongitude = 15.459148560238393
        } else {
            val locationGps =
                this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) as Location
            myLatitude = locationGps.latitude
            myLongitude = locationGps.longitude
        }

        mMap.apply {
            GlobalScope.async {
                this@HomeFragment.foodSharingApiClient = FoodSharingAPIClient()
                val foodSharingPoints =
                    this@HomeFragment.foodSharingApiClient.getNearbyFoodSharePoints(
                        myLatitude,
                        myLongitude
                    )
                for (foodSharingPoint in foodSharingPoints) {
                    val foodSharingPointLocation =
                        LatLng(foodSharingPoint.lat, foodSharingPoint.lon)
                    (activity as MainActivity).runOnUiThread {
                        this@HomeFragment.mMap.addMarker(
                            MarkerOptions().position(foodSharingPointLocation)
                                .title(foodSharingPoint.name).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
                            )
                        )
                    }
                }
            }

            val myLocation = LatLng(myLatitude, myLongitude)
            mMap.addMarker(MarkerOptions().position(myLocation).title("Marker at my location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13F))
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestLocation) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this)
            }
        }
    }
}
