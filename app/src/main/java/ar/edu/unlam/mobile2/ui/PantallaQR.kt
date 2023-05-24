package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.model.CountryModel
import coil.compose.rememberImagePainter
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.EnumMap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PantallaQR : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: CountriesViewModel by viewModels()

        setContent {
            pantallaInicio(viewModel = viewModel)
        }
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


    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @Composable
    fun pantallaInicio(viewModel: CountriesViewModel) {
        val context = LocalContext.current

        val countries = remember { mutableStateListOf<CountryModel>() }
        val limitedCountries = countries.take(15)
       GlobalScope.launch(Dispatchers.Main) {
            viewModel.startGame()
        }

        val combinedContent = buildString {
            for (country in limitedCountries) {
                appendLine("${country.translations}: ${country.capital}")
            }
        }

        val qrCodeBitmap: ImageBitmap? = try {
            generateQrCodeBitmap(combinedContent)?.asImageBitmap()
        } catch (e: WriterException) {
            null
        }
        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBarQR() },
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                qrCodeBitmap?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)

                    )
                }
                imagenLogo()
                Text(
                    text = "Escanea el siguiente QR",
                    color = Color.White,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }


    @Composable
    fun imagenLogo() {
        Image(
            painter = painterResource(id = R.drawable.mundo),
            contentDescription = "imagen logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        )
    }

    @Composable
    fun topBarQR(
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

