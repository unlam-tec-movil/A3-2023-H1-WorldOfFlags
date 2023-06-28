package ar.edu.unlam.mobile2.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.zxing.integration.android.IntentIntegrator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
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
                QRScannerScreen()
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
                    withContext(Dispatchers.Main) {
                        withContext(Dispatchers.Main) {
                            DatosJuego.listaPaises = countriesQR as List<CountryModel>
                            startActivity(Intent(context, PantallaJuegoVersus::class.java))
                        }
                    }
                }
                } else {
                    startActivity(Intent(this@PantallaScanearQr, PantallaPrincipal::class.java))
                }
            }

            @Composable
            fun QRScannerScreen() {
                qrScanActivityResult.launch(Intent(this, CaptureActivity::class.java))
            }
        }


