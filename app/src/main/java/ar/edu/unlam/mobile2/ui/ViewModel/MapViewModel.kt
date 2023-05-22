package ar.edu.unlam.mobile2.ui.ViewModel

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.model.MapState
import com.google.android.gms.location.FusedLocationProviderClient

class MapViewModel: ViewModel() {
	
	val state: MutableState<MapState> = mutableStateOf(
		MapState(
			lastKnowLocation = null
		)
	)
	@SuppressLint("MissingPermission")
	fun getDeviceLocation(fusedLocationProviderClient: FusedLocationProviderClient){
		val locationResult = fusedLocationProviderClient.lastLocation
		locationResult.addOnCompleteListener() { task ->
			if (task.isSuccessful){
				state.value = state.value.copy(
					lastKnowLocation = task.result
				)
			}
		}
	}
}