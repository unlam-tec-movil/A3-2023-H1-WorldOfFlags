package ar.edu.unlam.mobile2.ui
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

import ar.edu.unlam.mobile2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaQrViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class PantallaQR : ComponentActivity() {
    private val viewModel: PantallaQrViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                setContent {
                    launchCountries()
                }
            }

       private fun launchCountries() {
           lifecycleScope.launch {
               viewModel.StartGameWithQR()
               withContext(Dispatchers.Main) {
                   setContent {
                       PrincipalScreenQR( )
                   }
               }
           }
       }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable

    fun PrincipalScreenQR(  ) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopBarQR() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                ImagenLogo()
                Text(
                    text = "Escanea el siguiente QR",
                    color = Color.White,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 16.dp)
                )
                QRCode()
            }
        }
    }

    @Composable
    fun QRCode() {
        viewModel.qrCodeBitmap.value?.let { QRCode ->
            Image(
                bitmap = QRCode, contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }

    @Composable
    fun ImagenLogo() {
        Image(
            painter = painterResource(id = R.drawable.mundo),
            contentDescription = "imagen logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
    }

    @Composable
    fun TopBarQR(
    ) {
        var showMenu by remember {
            mutableStateOf(false)
        }
        TopAppBar(
            title = { Text(text = "QRs", modifier = Modifier, Color.White) },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            this@PantallaQR,
                            PantallaVersus::class.java
                        )
                    )
                    finish()
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "icono menu"
                    )
                }
                DropdownMenu(
                    expanded = showMenu, onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .width(110.dp)
                        .background(color = Color(0xFF335ABD)),
                )
                {
                }
            }
        )
    }
}

