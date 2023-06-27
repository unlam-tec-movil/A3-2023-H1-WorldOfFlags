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
        val randomCountries = countriesList?.shuffled()?.take(15)
        if (randomCountries != null) {
            for (country in randomCountries){
                contentBuilder.append(country.name.common).append("\n")
            }
        }
        return contentBuilder.toString()
    }
    
    suspend fun createCountryModelByName (name: String): List<CountryModel>{
        val countriesName: List<String> = processQRCodeContent(name)
        val countriesList: MutableList<CountryModel> = mutableListOf()
        for (countryName in countriesName){
            val country: CountryModel? = service.getCountryByName(countryName)?.get(0)
            if (country != null){
                countriesList.add(country)
            }
        }
        return countriesList
    }
    
    private fun generateQRCode(content: String): Bitmap? {
        Log.i("PantallaQRViewModel", "Estoy creando el QR")
        val hints: MutableMap<EncodeHintType, Any> = EnumMap(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 1024, 1024, hints)
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
    
    private fun processQRCodeContent(qrCodeContent: String): List<String> {
        val lines = qrCodeContent.split("\n")
        val countriesName = mutableListOf<String>()
        for (line in lines){
            val parts = line.split(",")
            if (parts.isNotEmpty()){
                val name = parts[0]
                countriesName.add(name)
            }
        }
        return countriesName
    }
}







