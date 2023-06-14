package ar.edu.unlam.mobile2.ui.ViewModel


import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.CountryModel
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PantallaQrViewModel @Inject constructor(private val service: CountriesService):ViewModel() {


   var CodeQRBitmap = MutableLiveData<Bitmap?>()
    val  countriesListQR = mutableListOf<CountryModel>()
     var json:String = ""
    @SuppressLint("SuspiciousIndentation")
    suspend fun StartGameWithQR() {
        val countriesList = service.getCountry()
        for (i in 0 until 15) {
            val RandomCountry = countriesList?.get(Random.nextInt(0, 250))
            if (RandomCountry != null) {
                countriesListQR.add(RandomCountry)
            }
        }

       val combinedContent = buildString {
            for (country in countriesListQR) {
                appendLine("Country: ${country.translations.spa.common}")
                appendLine("Capital: ${country.capital?.get(0)}")
                appendLine("Flag: ${country.flags.png}")
                appendLine("Latitude: ${country.latlng.get(0)}")
                appendLine("Longitude: ${country.latlng.get(1)}")
                appendLine()
            }
        }
      json = Gson().toJson(combinedContent)

      CodeQRBitmap.value = generateQrCodeBitmap(json)
        generateQrCodeBitmap(json)

    }


    @Throws(WriterException::class)
    suspend fun generateQrCodeBitmap(content: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(
            content,
            BarcodeFormat.QR_CODE,
            400,
            400
        )
    }
}



