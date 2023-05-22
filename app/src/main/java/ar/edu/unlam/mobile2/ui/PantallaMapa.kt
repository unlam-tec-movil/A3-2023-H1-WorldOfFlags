package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.edu.unlam.mobile2.model.MapState
import ar.edu.unlam.mobile2.ui.ViewModel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class PantallaMapa : ComponentActivity() {
	
	private val requestPermissionLauncher =
		registerForActivityResult(
			ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
			if (isGranted){
				viewModel.getDeviceLocation(fusedLocationClient)
			}
		}
	
	private fun askPermission() = when {
		ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
			viewModel.getDeviceLocation(fusedLocationClient)
		} else ->{
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
			MapViewScreen(
				state = viewModel.state.value
			)
		}
	}
	
	@Composable
	fun MapViewScreen(state: MapState) {
		val context = LocalContext.current
		val lat = intent.getDoubleExtra("latitude", 0.0)
		val lon = intent.getDoubleExtra("longitude", 0.0)

		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black),
			contentAlignment = Alignment.Center,
		) {
			Column(
				modifier = Modifier
					.padding(16.dp)
			) {
				if (state.lastKnowLocation != null){
					MapViewContainerWithUserLocation(state, lat = lat, lon = lon)
				} else {
					MapViewContainer(lat, lon)
				}
				Spacer(modifier = Modifier.height(16.dp))
				
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
	fun MapViewContainer(lat: Double, lon: Double) {
		val cameraPositionState = rememberCameraPositionState()
		GoogleMap(
			modifier = Modifier
				.fillMaxHeight(0.5f)
		)
		LaunchedEffect(Unit){
			cameraPositionState.centerOnLocation(lat,lon)
		}
	}
	
	@Composable
	fun MapViewContainerWithUserLocation(state: MapState, lat: Double, lon: Double){
		val markerUser = remember { mutableStateOf<LatLng?>(null) }
		val markerCountry = remember { mutableStateOf<LatLng?>(null) }
		val mapProperties = MapProperties(
			isMyLocationEnabled = state.lastKnowLocation != null,
		)
		val cameraPositionState = rememberCameraPositionState()
		
		LaunchedEffect(Unit) {
			if (state.lastKnowLocation != null) {
				markerUser.value = LatLng(state.lastKnowLocation.latitude, state.lastKnowLocation.longitude)
				markerCountry.value = LatLng(lat, lon)
				
				val boundsBuilder = LatLngBounds.Builder()
				boundsBuilder.include(markerCountry.value!!)
				boundsBuilder.include(markerUser.value!!)
				val bounds = boundsBuilder.build()
				
				val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0)
				
				// Mueve la cámara para mostrar los marcadores dentro de los límites
				cameraPositionState.animate(
					update = cameraUpdate
				)
			}
		}
			GoogleMap(
				modifier = Modifier
					.fillMaxHeight(0.5f),
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
	
	private suspend fun CameraPositionState.centerOnLocation(lat: Double, lon: Double) =
		animate(
			update = CameraUpdateFactory.newLatLngZoom(
				LatLng(lat, lon),
				0f
			)
		)
	
}