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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_qr),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Escanea el siguiente QR",
                        color = Color(0xFF105590),
                        modifier = Modifier.padding(top = 16.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    if (codeQRGenerated) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            QRCode(codeQR)
                            StartButton(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                context
                            )
                        }
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
    }
    
    @Composable
    fun StartButton(modifier: Modifier, context: Context) {
        androidx.compose.material3.Button(
            onClick = {
                lifecycleScope.launch {
                    val content: String = viewModel.qrCodeContent.value!!
                    withContext(Dispatchers.Main) {
                        countriesQR = viewModel.createCountryModelByName(content)
                        DatosJuego.listaPaises = countriesQR as List<CountryModel>
                        startActivity(Intent(context, PantallaJuegoVersus::class.java))
                    }
                }
            },
            modifier = modifier
                .height(100.dp)
                .width(500.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(Color.Transparent),
        ) {
            Image(
                painter = painterResource(id = R.drawable.comenzar_versus),
                contentDescription = "imagenPista",
                modifier = Modifier
                    .size(120.dp),
            )
        }
    }
    
    @Composable
    fun QRCode(codeQR: Bitmap?) {
        codeQR?.let { QRCode ->
            Image(
                bitmap = QRCode.asImageBitmap(), contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
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


