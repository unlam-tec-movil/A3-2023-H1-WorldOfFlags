package ar.edu.unlam.mobile2.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.zxing.integration.android.IntentIntegrator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.model.CountryModel
import ar.edu.unlam.mobile2.model.DatosJuego
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaQrViewModel
import com.journeyapps.barcodescanner.CaptureActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val qrScanActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            val result = IntentIntegrator.parseActivityResult(result.resultCode, data)
            if (result != null && result.contents != null) {
                val context = this
                lifecycleScope.launch {
                    countriesQR = viewModel.createCountryModelByName(result.contents)
                    withContext(Dispatchers.Main){
                        DatosJuego.listaPaises = countriesQR as List<CountryModel>
                        startActivity(Intent(context, PantallaJuegoVersus::class.java))
                    }
                }
            }
        }
    @Composable
    fun QRScannerScreen() {
        qrScanActivityResult.launch(Intent(this, CaptureActivity::class.java))
    }
}
