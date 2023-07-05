package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.ui.ViewModel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@AndroidEntryPoint
class PantallaMapa : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationClient)
            } else {
                viewModel.setShowComposableWithUserLocationIfDeniedPermission()
            }
        }

    private fun askPermission() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            viewModel.getDeviceLocation(fusedLocationClient)
        }

        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        askPermission()
        setContent {
            val lastKnowLocation = viewModel.state.value.lastKnowLocation
            val showComposableWithUserLocation =
                viewModel.state.value.showComposableWithUserLocation
            val cameraPositionState = rememberCameraPositionState()

            val lat = intent.getDoubleExtra("latitude", 0.0)
            val lon = intent.getDoubleExtra("longitude", 0.0)
            val versus = intent.getBooleanExtra("versus", false)
            val index = intent.getIntExtra("index", 0)
            val vidas = intent.getIntExtra("vidas", 5)
            val puntos =  intent.getIntExtra("puntos", 0)
            val paisesAcertados = intent.getIntExtra("paisesAcertados",0)
            val cancelarMovimiento =intent.getBooleanExtra("cancelarMovimiento",true)
            val marker = LatLng(lat, lon)

            MapViewScreen(
                lat = lat,
                lon = lon,
                lastKnowLocation,
                showComposableWithUserLocation,
                marker,
                cameraPositionState,
                vidas,
                puntos,
                paisesAcertados,
                cancelarMovimiento,
                versus,
                index
            )
        }
    }

    @Composable
    fun MapViewScreen(
        lat: Double,
        lon: Double,
        lastKnowLocation: Location?,
        showComposableWithUserLocation: Boolean,
        marker: LatLng,
        cameraPositionState: CameraPositionState,
        vidas: Int,
        puntos: Int,
        paisesAcertados: Int,
        cancelarMovimiento: Boolean,
        versus: Boolean,
        index: Int
    ) {
        val intent = if (versus){
            Intent(this, PantallaJuegoVersus::class.java)
        } else {
            Intent(this, PantallaJuego::class.java)
        }
        intent.putExtra("index", index)
        intent.putExtra("vidas", vidas)
        intent.putExtra("puntos", puntos)
        intent.putExtra("paisesAcertados",paisesAcertados)
        intent.putExtra("cancelarMovimiento",cancelarMovimiento)
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_qr),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (lastKnowLocation != null && showComposableWithUserLocation) {
                    MapViewContainerWithUserLocation(lastKnowLocation, lat = lat, lon = lon,cameraPositionState)
                    Spacer(modifier = Modifier.padding(15.dp))
                    showDistance(lat = lat, lon = lon)
                } else {
                    MapViewContainer(marker)
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Button(
                    onClick = {
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(500.dp),
                    shape = RoundedCornerShape(50),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.Transparent),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.siguiente),
                        contentDescription = "imagenPista",
                        modifier = Modifier
                            .size(120.dp),
                    )
                }
            }
        }
    }


    private fun calculateDistance(lat: Double, lon: Double): Int {
        val lastKnowLocation = viewModel.state.value.lastKnowLocation

        val lat1 = lastKnowLocation?.latitude
        val lon1 = lastKnowLocation?.longitude
        val lat2 = lat
        val lon2 = lon
        return (viewModel.calculateDistance(lat1, lon1, lat2, lon2) * 100).roundToInt() / 100
    }

    @Composable
    fun showDistance(lat: Double, lon: Double) {
        Text(
            text = "Usted se encuentra a ${
                calculateDistance(
                    lat,
                    lon
                )
            } KM del pais acertado",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
            )
    }
    
@Composable
fun MapViewContainer(marker: LatLng) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(marker, 0f)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(position = marker)
            rememberCameraPositionState()
        }
    }

}
@Composable
fun MapViewContainerWithUserLocation(
    lastKnowLocation: Location?,
    lat: Double,
    lon: Double,
    cameraPositionState: CameraPositionState
) {
    val markerUser = remember { mutableStateOf<LatLng?>(null) }
    val markerCountry = remember { mutableStateOf<LatLng?>(null) }
    val mapProperties = MapProperties(
        isMyLocationEnabled = lastKnowLocation != null,
    )

    LaunchedEffect(Unit) {
        if (lastKnowLocation != null) {
            markerUser.value =
                LatLng(lastKnowLocation.latitude, lastKnowLocation.longitude)
            markerCountry.value = LatLng(lat, lon)

            cameraPositionState.centerOnLocation(markerCountry.value!!)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxHeight(0.75f) // Mapa ocupa el 75% de la altura
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            markerUser.value?.let { userLatLng ->
                Marker(position = userLatLng)
            }
            markerCountry.value?.let { countryLatLng ->
                Marker(position = countryLatLng)
            }
        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(latLng: LatLng) =
    withContext(Dispatchers.Main) {
        animate(
            update = CameraUpdateFactory.newLatLngZoom(
                latLng,
                0f
            )
        )
    }
}

