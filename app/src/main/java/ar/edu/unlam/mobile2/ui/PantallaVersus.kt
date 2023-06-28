package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ar.edu.unlam.mobile2.R


class PantallaVersus : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scaffoldState = rememberScaffoldState()

            pantallaInicio(scaffoldState)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun pantallaInicio(scaffoldState: ScaffoldState) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        )
        {
            Image(
                painter = painterResource(id = R.drawable.fondo_2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            topBarVersus()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScanearQR()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    MostrarQR()
                }
            }
        }
    }


    @Composable
    fun ScanearQR() {
        androidx.compose.material3.Button(
            onClick = { startActivity(Intent(this@PantallaVersus, PantallaScanearQr::class.java)) },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.escanear),
                contentDescription = "imagenPista",
                modifier = Modifier
                    .size(120.dp)
            )
        }
    }
    @Composable
    fun MostrarQR() {
        androidx.compose.material3.Button(
            onClick = {
                startActivity(Intent(this@PantallaVersus, PantallaQR::class.java))
                finish()
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.generar),
                contentDescription = "imagenPista",
                modifier = Modifier
                    .size(120.dp),
            )
        }
    }
    @Composable
    fun topBarVersus(
    ) {
        var showMenu by remember {
            mutableStateOf(false)
        }
        TopAppBar(
            title = { Text(text = "", modifier = Modifier, Color.White) },
            backgroundColor = Color.Transparent,
            actions = {
                IconButton(onClick = {   startActivity(Intent(this@PantallaVersus,
                    MainActivity::class.java))
                    finish()}) {
                    Image(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_black_24),
                        contentDescription = "icono menu")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .width(110.dp)
                        .background(color = Color(0xFF335ABD)),)
                {
                }
            }
        )
    }

}