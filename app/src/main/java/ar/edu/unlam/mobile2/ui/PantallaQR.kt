package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaQrViewModel
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.encoder.QRCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.EnumMap

@AndroidEntryPoint

class PantallaQR : ComponentActivity() {
    private val viewModel: PantallaQrViewModel by viewModels()
    @SuppressLint("SuspiciousIndentation", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            launchCountries()

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

    fun PrincipalScreenQR( ) {
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
            viewModel.CodeQRBitmap.value?.let { QRCode ->
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

