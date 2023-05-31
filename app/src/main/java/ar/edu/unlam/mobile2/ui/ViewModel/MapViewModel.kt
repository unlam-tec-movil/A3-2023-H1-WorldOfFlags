package ar.edu.unlam.mobile2.ui.ViewModel

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.model.MapState
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.math.pow

class MapViewModel: ViewModel() {
	
	val state: MutableState<MapState> = mutableStateOf(
		MapState(
			lastKnowLocation = null,
			showComposableWithUserLocation = true
		)
	)


	@SuppressLint("MissingPermission")
	fun getDeviceLocation(fusedLocationProviderClient: FusedLocationProviderClient){
		val locationResult = fusedLocationProviderClient.lastLocation
		locationResult.addOnCompleteListener() { task ->
			if (task.isSuccessful){
				state.value = state.value.copy(
					lastKnowLocation = task.result,
					showComposableWithUserLocation = task.result != null
				)
			}
		}
	}
	
	fun setShowComposableWithUserLocationIfDeniedPermission(){
		state.value = state.value.copy(
			showComposableWithUserLocation = false
		)
	}

	fun calculateDistance(lat1: Double?, lon1: Double?, lat2: Double, lon2: Double): Double {
		val earthRadius = 6371.0 // Radio de la Tierra en kilómetros

		// Convertir las latitudes y longitudes a radianes
		val lat1Rad = lat1?.let { Math.toRadians(it) }
		val lon1Rad = lon1?.let { Math.toRadians(it) }
		val lat2Rad = Math.toRadians(lat2)
		val lon2Rad = Math.toRadians(lon2)

		// Diferencias de latitud y longitud
		val dLat = lat2Rad - lat1Rad!!
		val dLon = lon2Rad - lon1Rad!!

		// Fórmula del haversine
		val a = kotlin.math.sin(dLat / 2).pow(2) + kotlin.math.cos(lat1Rad) * kotlin.math.cos(lat2Rad) * kotlin.math.sin(dLon / 2).pow(2)
		val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

		// Distancia en kilómetros
		val distance = earthRadius * c

		return distance
	}
}