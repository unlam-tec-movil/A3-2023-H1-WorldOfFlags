package ar.edu.unlam.mobile2.ui.ViewModel


import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.CountryModel

import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.encoder.QRCode
import com.journeyapps.barcodescanner.BarcodeEncoder

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.EnumMap
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PantallaQrViewModel @Inject constructor(private val service: CountriesService):ViewModel() {

    private var qrCodeBitmap: Bitmap? = null
   var CodeQRBitmap = MutableLiveData<Bitmap?>()
    val  countriesListQR = mutableListOf<CountryModel>()
     var json:String = ""
    suspend fun StartGameWithQR() {
        val countriesList = service.getCountry()
        for (i in 0 until 15) {
            val correctCountry = countriesList?.get(Random.nextInt(0, 250))
            if (correctCountry != null) {
                countriesListQR.add(correctCountry)
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
/*
data class Country(
    val name: String,
    val capital: String?,
    val flagUrl: String?,
    val latitude: Double?,
    val longitude: Double?
)
*/


