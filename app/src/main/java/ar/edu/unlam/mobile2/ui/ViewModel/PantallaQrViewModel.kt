package ar.edu.unlam.mobile2.ui.ViewModel


import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.CountryModel
import ar.edu.unlam.mobile2.model.FlagsModel
import ar.edu.unlam.mobile2.model.TranslationsModel
import ar.edu.unlam.mobile2.model.TranslationsOptionsModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.EnumMap
import javax.inject.Inject

@HiltViewModel
class PantallaQrViewModel @Inject constructor(private val service: CountriesService):ViewModel() {
    
    var codeQRBitmap = MutableLiveData<Bitmap?>()
    var codeQRGenerated = mutableStateOf(false)
    var qrCodeContent = MutableLiveData<String?>()
    fun generateQR(){
        viewModelScope.launch {
            Log.i("PantallaQRViewModel", "Yendo a generar el QR")
            qrCodeContent.value = generateQRCodeContent()
            codeQRBitmap.value = generateQRCode(qrCodeContent.value!!)
        }
    }
    
    suspend fun generateQRCodeContent(): String {
        Log.i("PantallaQRViewModel", "Estoy generando el contenido")
        val contentBuilder = StringBuilder()
        val countriesList = service.getCountry()
        val randomCountries = countriesList?.shuffled()?.take(3)
        if (randomCountries != null) {
            for (country in randomCountries){
                val capital = country.capital.joinToString(",")
                val latlng = country.latlng.joinToString(",")
                contentBuilder.append("$capital,${country.flags.svg},${country.flags.png},${country.translations.por.official},${country.translations.por.common},${country.translations.spa.official},${country.translations.spa.common},${country.region},${country.subregion},$latlng\n")
            }
        }
        return contentBuilder.toString()
    }
    
    private fun generateQRCode(content: String): Bitmap? {
        Log.i("PantallaQRViewModel", "Estoy creando el QR")
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            codeQRGenerated.value = true
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    
    fun processQRCodeContent(qrCodeContent: String): List<CountryModel> {
        val lines = qrCodeContent.split("\n")
        val countries = mutableListOf<CountryModel>()
        for (line in lines) {
            val parts = line.split(",")
            if (parts.size == 11) {
                val capital = parts[0].split(",").toTypedArray()
                val flagSvg = parts[1]
                val flagPng = parts[2]
                val translationsPorOfficial = parts[3]
                val translationsPorCommon = parts[4]
                val translationsSpaOfficial = parts[5]
                val translationsSpaCommon = parts[6]
                val region = parts[7]
                val subregion = parts[8]
                val lat = parts[9].toDouble()
                val lng = parts[10].toDouble()
                
                val latLng = arrayOf(lat,lng)
                
                val country = CountryModel(
                    capital,
                    FlagsModel(flagSvg, flagPng),
                    TranslationsModel(
                        TranslationsOptionsModel(translationsPorOfficial, translationsPorCommon),
                        TranslationsOptionsModel(translationsSpaOfficial, translationsSpaCommon)
                    ),
                    region,
                    subregion,
                    latLng
                )
                countries.add(country)
            }
        }
        return countries
    }
    
}







