package ar.edu.unlam.mobile2.ui.ViewModel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.CountryModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PantallaQrViewModel @Inject constructor(private val service: CountriesService):ViewModel() {

    var qrCodeBitmap = MutableLiveData<ImageBitmap>()
    val  countriesListQR = mutableListOf<CountryModel>()
    suspend fun StartGameWithQR() {
        val countriesList = service.getCountry()
       for(i in 0 .. 14){
           val correctCountry = countriesList?.get(Random.nextInt(0, 250))
           if (correctCountry != null) {
               countriesListQR.add(correctCountry)
           }
        }
        val combinedContent = buildString {
            if (countriesListQR != null) {
                for (country in countriesListQR) {
                    appendLine("${country.translations.spa.common}: ${country.capital?.get(0)}")
                }
            }
        }
        qrCodeBitmap.value=generateQrCodeBitmap(combinedContent)?.asImageBitmap()
    }

    @Throws(WriterException::class)
    fun generateQrCodeBitmap(content: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(
            content,
            BarcodeFormat.QR_CODE,
            250,
            250
        )
    }

    suspend fun get15Countries(): List<CountryModel> {
        val countriesList = service.getCountry()
        val countries15 = mutableListOf<CountryModel>()
        for (i in 0..14) {
            val correctCountry = countriesList?.get(Random.nextInt(0, 250))
            if (correctCountry != null) {
                countries15.add(correctCountry)
            }
        }
        return countries15
    }
}





