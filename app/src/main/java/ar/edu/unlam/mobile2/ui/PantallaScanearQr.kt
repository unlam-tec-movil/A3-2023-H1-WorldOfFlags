package ar.edu.unlam.mobile2.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.zxing.integration.android.IntentIntegrator
import androidx.compose.runtime.mutableStateOf


class PantallaScanearQr : ComponentActivity() {
    private val scannedText = mutableStateOf("")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelado", Toast.LENGTH_SHORT).show()
            } else {
                scannedText.value = result.contents
                Toast.makeText(this, "el valor es :${result.contents}", Toast.LENGTH_SHORT).show()
                //ViewModel.getCountriesFromQR(result.contents)
                //startActivity(Intent(this@PantallaScanearQr, PantallaJuegoVersus::class.java))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }




    @Composable
    fun QRScannerScreen() {
             initScanner()
    }
}
