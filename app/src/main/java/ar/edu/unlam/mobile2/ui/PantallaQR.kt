package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

import ar.edu.unlam.mobile2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.model.CountryModel
import ar.edu.unlam.mobile2.model.DatosJuego
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaQrViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext


@AndroidEntryPoint

class PantallaQR : ComponentActivity() {
    private val viewModel: PantallaQrViewModel by viewModels()
    private var countriesQR: List<CountryModel>? by mutableStateOf(null)
    
    @SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        launchCountries()
        
    }
    
    private fun launchCountries() {
        lifecycleScope.launch {
            viewModel.generateQR()
            withContext(Dispatchers.Main) {
                setContent {
                    val context = LocalContext.current
                    val codeQRGenerated = viewModel.codeQRGenerated.value
                    val codeQR = viewModel.codeQRBitmap.value
                    PrincipalScreenQR(codeQR, codeQRGenerated, context)
                }
            }
        }
    }
    
    
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    
    fun PrincipalScreenQR(codeQR: Bitmap?, codeQRGenerated: Boolean, context: Context) {
        
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
                if (codeQRGenerated) {
                    StartButton(modifier = Modifier, context)
                    QRCode(codeQR)
                } else {
                    // Muestra un indicador de carga mientras se genera el QR
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(CenterHorizontally)
                    )
                }
            }
        }
    }
    
    @Composable
    fun StartButton(modifier: Modifier, context: Context) {
        Button(
            modifier = modifier
                .height(50.dp)
                .width(180.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF396AE9)),
            onClick = {
                lifecycleScope.launch {
                    val content: String = viewModel.generateQRCodeContent()
                    withContext(Dispatchers.Main){
                        countriesQR = viewModel.processQRCodeContent(content)
                        DatosJuego.listaPaises = countriesQR as List<CountryModel>
                        startActivity(Intent(context, PantallaJuegoVersus::class.java))
                    }
                }
            }) {
            androidx.compose.material.Text(text = "Comenzar Versus")
        }
    }
    
    @Composable
    fun QRCode(codeQR: Bitmap?) {
        codeQR?.let { QRCode ->
            Image(
                bitmap = QRCode.asImageBitmap(), contentDescription = "",
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

