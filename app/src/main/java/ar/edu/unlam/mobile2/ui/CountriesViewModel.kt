package ar.edu.unlam.mobile2.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile2.HttpClientModule
import ar.edu.unlam.mobile2.domain.CountriesService
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.random.Random

@HiltViewModel
class CountriesViewModel @Inject constructor(private val service: CountriesService) : ViewModel() {


    //A partir de la lista de países, agarro dos, uno correcto y uno incorrecto
    val incorrectCountryNameInGame = MutableLiveData<String>()
    val correctCountryFlagInGame = MutableLiveData<String>()
    val correctCountryNameInGame = MutableLiveData<String>()

    suspend fun startGame(){

        val countriesList = service.getCountry()
        val correctCountry = countriesList?.get(Random.nextInt(0,250))
        correctCountryNameInGame.value = correctCountry?.translations?.spa?.common
        correctCountryFlagInGame.value = correctCountry?.flags?.png
        val incorrectCountry = countriesList?.get(Random.nextInt(0,250))
        if (!incorrectCountry?.equals(correctCountry)!!){
            incorrectCountryNameInGame.value = incorrectCountry.translations.spa.common
        }
    }

    fun getImageRequest(context: Context): ImageRequest {
        Log.i("CountriesViewModel", "obteniendo  ImageRequest:$correctCountryFlagInGame")
        val imageLoader: ImageLoader = initUntrustImageLoader(context)

        val request = ImageRequest.Builder(context)
            .data(correctCountryFlagInGame)
            .crossfade(true)
            .build()

        viewModelScope.launch {
            Log.i("MainViewModel", "Ejecutando  ImageRequest")
            imageLoader.execute(request)
        }
        return request
    }

    private fun initUntrustImageLoader(context: Context): ImageLoader {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }.build()

        var module = HttpClientModule()

        /*return ImageLoader.Builder(context)
            .okHttpClient(client)
            .build()*/
        return module.imageLoader(context)
    }
}
