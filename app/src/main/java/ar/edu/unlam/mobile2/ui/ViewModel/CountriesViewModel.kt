package ar.edu.unlam.mobile2.ui.ViewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.CountryModel
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CountriesViewModel @Inject constructor(private val service: CountriesService) : ViewModel() {


    //A partir de la lista de países, agarro dos, uno correcto y uno incorrecto
    val incorrectCountryNameInGame = MutableLiveData<String>()
    val correctCountryFlagInGame = MutableLiveData<String>()
    val correctCountryCapitalInGame = MutableLiveData<String>()
    val correctCountryNameInGame = MutableLiveData<String>()
    val latitudeCorrectCountryGame = MutableLiveData<Double>()
    val longitudeCorrectCountryGame = MutableLiveData<Double>()


    suspend fun startGame() {
            val countriesList = service.getCountry()
            val correctCountry = countriesList?.get(Random.nextInt(0, 250))
            correctCountryNameInGame.value = correctCountry?.translations?.spa?.common
            correctCountryFlagInGame.value = correctCountry?.flags?.png
            correctCountryCapitalInGame.value = correctCountry?.capital?.get(0)
            val incorrectCountry = countriesList?.get(Random.nextInt(0, 250))
            if (!incorrectCountry?.equals(correctCountry)!!) {
                latitudeCorrectCountryGame.value = correctCountry?.latlng?.get(0)
                longitudeCorrectCountryGame.value = correctCountry?.latlng?.get(1)
                val incorrectCountry = countriesList?.get(Random.nextInt(0, 250))
                if (!incorrectCountry?.equals(correctCountry)!!) {
                    incorrectCountryNameInGame.value = incorrectCountry.translations.spa.common
                }
            }
        }
    
    suspend fun startGameQR(country: CountryModel){
        var incorrectCountry: CountryModel
        correctCountryNameInGame.value = country.translations.spa.common
        correctCountryFlagInGame.value = country.flags.png
        correctCountryCapitalInGame.value = country.capital[0]
        latitudeCorrectCountryGame.value = country.latlng[0]
        longitudeCorrectCountryGame.value = country.latlng[1]
        do {
            incorrectCountry = service.getCountry()?.get(Random.nextInt(0, 250))!!
            incorrectCountryNameInGame.value = incorrectCountry.translations.spa.common
        } while (incorrectCountry == country)
    }
}

