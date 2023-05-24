package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ar.edu.unlam.mobile2.model.MapState
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
            val lat = intent.getDoubleExtra("latitude", 0.0)
            val lon = intent.getDoubleExtra("longitude", 0.0)
            MapViewScreen(
                state = viewModel.state.value,
                lat = lat,
                lon = lon
            )
        }
    }

    @Composable
    fun MapViewScreen(state: MapState, lat: Double, lon: Double) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.lastKnowLocation != null && state.showComposableWithUserLocation) {
                    MapViewContainerWithUserLocation(state, lat = lat, lon = lon)
                    Spacer(modifier = Modifier.padding(15.dp))
                    calculateDistance(state, lat = lat, lon = lon)
                } else {
                    MapViewContainer(lat, lon)
                }
                Spacer(modifier = Modifier.padding(15.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        context.startActivity(Intent(context, PantallaJuego::class.java))
                    }
                ) {
                    Text(text = "Siguiente")
                }
            }
        }
    }

    @Composable
    private fun calculateDistance(state: MapState, lat: Double, lon: Double) {

        val lat1 = state.lastKnowLocation?.latitude
        val lon1 = state.lastKnowLocation?.longitude
        val lat2 = lat
        val lon2 = lon

        val distance = (viewModel.calculateDistance(lat1, lon1, lat2, lon2) * 100).roundToInt() /100

        Text(
            text = "La distancia entre tu ubicacion y el pais es de $distance KM",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Yellow,
            textAlign = TextAlign.Center,
            fontSize = 18.sp ,

        )
    }


    @Composable
    fun MapViewContainer(lat: Double, lon: Double) {
        val marker = LatLng(lat, lon)
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
    fun MapViewContainerWithUserLocation(state: MapState, lat: Double, lon: Double) {
        val markerUser = remember { mutableStateOf<LatLng?>(null) }
        val markerCountry = remember { mutableStateOf<LatLng?>(null) }
        val mapProperties = MapProperties(
            isMyLocationEnabled = state.lastKnowLocation != null,
        )
        val cameraPositionState = rememberCameraPositionState()

        LaunchedEffect(Unit) {
            if (state.lastKnowLocation != null) {
                markerUser.value =
                    LatLng(state.lastKnowLocation.latitude, state.lastKnowLocation.longitude)
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