package ar.edu.unlam.mobile2.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.zxing.integration.android.IntentIntegrator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ar.edu.unlam.mobile2.model.CountryModel
import ar.edu.unlam.mobile2.model.DatosJuego
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaQrViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PantallaScanearQr : ComponentActivity() {
    private val viewModel: PantallaQrViewModel by viewModels()
    private var countriesQR: List<CountryModel>? by mutableStateOf(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                QRScannerScreen ()
            }
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Busca un Codigo Qr")
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            countriesQR = viewModel.processQRCodeContent(result.contents)
            DatosJuego.listaPaises = countriesQR as List<CountryModel>
            startActivity(Intent(this, PantallaJuegoVersus::class.java))
        }
    }
    
    @Composable
    fun QRScannerScreen() {
        initScanner()
    }
}
