package ar.edu.unlam.mobile2.model

import android.location.Location

data class MapState(
	val lastKnowLocation: Location?,
	var showComposableWithUserLocation: Boolean
)
