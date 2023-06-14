package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.movimiento.DetectarMovimiento
import ar.edu.unlam.mobile2.movimiento.TiltDirection
import ar.edu.unlam.mobile2.ui.ViewModel.CountriesViewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@AndroidEntryPoint
class PantallaJuego : ComponentActivity() {
    private lateinit var motionDetector: DetectarMovimiento
    private val countriesViewModel: CountriesViewModel by viewModels()
    private var vidas: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        vidas = intent.getIntExtra("vidas", 5)
        
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        motionDetector = DetectarMovimiento(this)
        motionDetector.start()
        launchCountries()
    }

    private fun launchCountries() {
        val progressDialog = AlertDialog.Builder(this@PantallaJuego)
            .setView(R.layout.layout_loading)
            .setCancelable(false)
            .create()
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        progressDialog.show()
        
        lifecycleScope.launch {
            countriesViewModel.startGame()
            withContext(Dispatchers.Main) {
                progressDialog.dismiss()
                
                setContent {
                    val flag = countriesViewModel.correctCountryFlagInGame.value
                    val correctCountryNameInGame = countriesViewModel.correctCountryNameInGame.value
                    val incorrectCountryNameInGame =
                        countriesViewModel.incorrectCountryNameInGame.value
                    val correctCountryCapitalInGame =
                        countriesViewModel.correctCountryCapitalInGame.value
                    val tiltDirection = motionDetector.tiltDirection.collectAsState()
                    val latitudeCorrectCountryGame =
                        countriesViewModel.latitudeCorrectCountryGame.value
                    val longitudeCorrectCountryGame =
                        countriesViewModel.longitudeCorrectCountryGame.value
                    if (flag != null && correctCountryNameInGame != null && incorrectCountryNameInGame != null && correctCountryCapitalInGame != null && latitudeCorrectCountryGame != null && longitudeCorrectCountryGame != null) {
                        PrincipalScreen(
                            flag,
                            correctCountryNameInGame,
                            incorrectCountryNameInGame,
                            correctCountryCapitalInGame,
                            tiltDirection,
                            latitudeCorrectCountryGame,
                            longitudeCorrectCountryGame,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun PrincipalScreen(
        flag: String,
        correctCountryNameInGame: String,
        incorrectCountryNameInGame: String,
        correctCountryCapitalInGame: String,
        tiltDirection: State<TiltDirection>,
        latitudeCorrectCountryGame: Double,
        longitudeCorrectCountryGame: Double,
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .rotate(0F)
        ) {
            TopBarQR()
            TopBlock(flag)
            Divider(
                color = Color.DarkGray,
                thickness = 5.5.dp,
                modifier = Modifier.padding(top = 25.dp)
            )
            BottomBlock(
                correctCountryNameInGame,
                incorrectCountryNameInGame,
                tiltDirection, latitudeCorrectCountryGame,
                longitudeCorrectCountryGame
            )
            Spacer(modifier = Modifier.padding(65.dp))
            ShowCapital(correctCountryCapitalInGame)
        }
    }

    @Composable
    fun TopBlock(flag: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 20.dp, end = 20.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xFF335ABD))
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Foto de perfil del usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 14.dp, top = 5.dp, bottom = 5.dp)
                        .size(57.dp)
                        .clip(CircleShape)
                )
                //-------------------------------------------------------------------------------------------------------------------------------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 20.dp, top = 7.dp)
                ) {
                    Text(text = "Nombre", color = Color.White, fontSize = 17.sp)
                    Text("Nacionalidad", color = Color.White, fontSize = 17.sp)
                }
                //----------------------------------------------------------------------------------------------------------------------------------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 60.dp, top = 7.dp)
                ) {
                    Text(text = "Puntos : 0", color = Color.White, fontSize = 17.sp)
                    Text("Vidas : $vidas", color = Color.White, fontSize = 17.sp)
                }
                //----------------------------------------------------------------------------------------------------------------------------------------------
            }
//-------------------------------------------------------------------------------------------------------------------------------------------------------
            AsyncImage(
                model = flag,
                contentDescription = "Bandera",
                modifier = Modifier
                    .size(300.dp, 170.dp)
                    .padding(start = 17.dp, end = 17.dp, top = 20.dp)
                    .fillMaxWidth()
            )
        }
    }

    private var buttonIsVisible by mutableStateOf(true)
    private var capitalVisibility by mutableStateOf(false)

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun BottomBlock(
        correctCountryNameInGame: String,
        incorrectCountryNameInGame: String,
        tiltDirection: State<TiltDirection>,
        latitudeCorrectCountryGame: Double,
        longitudeCorrectCountryGame: Double
    ) {
        val intent = Intent(this, PantallaMapa::class.java)
        intent.putExtra("latitude", latitudeCorrectCountryGame)
        intent.putExtra("longitude", longitudeCorrectCountryGame)
        intent.putExtra("vidas", vidas)
        when (Random.nextInt(from = 1, until = 3)) {
            1 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 110.dp)
                    ) {
                        Column(
                            Modifier.width(150.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Boton para el país correcto
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        this@PantallaJuego,
                                        "¡Correcto!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val progressDialog = AlertDialog.Builder(this@PantallaJuego)
                                        .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
                                        .setCancelable(false)
                                        .create()
                                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                    progressDialog.show()
            
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        progressDialog.dismiss()
                                        startActivity(intent)
                                        buttonIsVisible = true
                                        capitalVisibility = false
                                    }, 2000)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent)
                            ) {
                                Text(
                                    text = correctCountryNameInGame,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 23.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                when (tiltDirection.value) {
                                    TiltDirection.LEFT -> {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "¡Correcto!",
                                            Toast.LENGTH_SHORT
                                        ).show()
    
                                        val progressDialog = AlertDialog.Builder(this@PantallaJuego)
                                            .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
                                            .setCancelable(false)
                                            .create()
                                        progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                        progressDialog.show()
    
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            progressDialog.dismiss()
        
                                            startActivity(intent)
                                            buttonIsVisible = true
                                            capitalVisibility = false
                                        }, 2000)
                                    }

                                    else -> {
                                    }
                                }
                            }
                        }
                        Divider(
                            Modifier
                                .width(110.dp)
                                .size(height = 45.dp, width = 1.dp)
                                .rotate(90f)
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color.Gray,
                            thickness = 1.dp,
                        )
                        Column(
                            Modifier.width(150.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Boton para el país incorrecto
                            Button(
                                onClick = {
                                    if (vidas > 1) {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "Incorrecto :(",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        launchCountries()
                                        Thread.sleep(1500)
                                        buttonIsVisible = true
                                        capitalVisibility = false
                                        this@PantallaJuego.vidas -= 1
                                    } else {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "Game Over",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Thread.sleep(1500)
                                        startActivity(Intent(this@PantallaJuego, PantallaPrincipal::class.java))
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent)
                            ) {
                                Text(
                                    text = incorrectCountryNameInGame,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 23.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                when (tiltDirection.value) {

                                    TiltDirection.RIGHT -> {
                                        if (vidas > 0) {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "Incorrecto :(",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            launchCountries()
                                            Thread.sleep(1500)
                                            buttonIsVisible = true
                                            capitalVisibility = false
                                            this@PantallaJuego.vidas -= 1
                                        } else {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "Game Over",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Thread.sleep(1500)
                                            startActivity(Intent(this@PantallaJuego, PantallaPrincipal::class.java))
                                        }
                                    }

                                    else -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 110.dp)
                    ) {
                        Column(
                            Modifier.width(150.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Boton para el país incorrecto
                            Button(
                                onClick = {
                                    if (vidas > 0) {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "Incorrecto :(",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        launchCountries()
                                        Thread.sleep(1500)
                                        buttonIsVisible = true
                                        capitalVisibility = false
                                        this@PantallaJuego.vidas -= 1
                                    } else {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "Game Over",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        Thread.sleep(1500)
                                        startActivity(Intent(this@PantallaJuego, PantallaPrincipal::class.java))
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent)
                            ) {
                                Text(
                                    text = incorrectCountryNameInGame,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 23.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                when (tiltDirection.value) {
                                    TiltDirection.LEFT -> {
                                        if (vidas > 0) {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "Incorrecto :(",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            launchCountries()
                                            Thread.sleep(1500)
                                            buttonIsVisible = true
                                            capitalVisibility = false
                                            this@PantallaJuego.vidas -= 1
                                        } else {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "Game Over",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Thread.sleep(1500)
                                            startActivity(Intent(this@PantallaJuego, PantallaPrincipal::class.java))
                                        }
                                    }

                                    else -> {
                                    }
                                }

                            }
                        }
                        Divider(
                            Modifier
                                .width(110.dp)
                                .size(height = 45.dp, width = 1.dp)
                                .rotate(90f)
                                .padding(vertical = 6.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = Color.Gray,
                            thickness = 1.dp,
                        )
                        Column(
                            Modifier.width(150.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Boton para el país correcto
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        this@PantallaJuego,
                                        "¡Correcto!",
                                        Toast.LENGTH_SHORT
                                    ).show()
    
                                    val progressDialog = AlertDialog.Builder(this@PantallaJuego)
                                        .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
                                        .setCancelable(false)
                                        .create()
                                    progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                    progressDialog.show()
    
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        progressDialog.dismiss()
                                        startActivity(intent)
                                        buttonIsVisible = true
                                        capitalVisibility = false
                                    }, 2000)
                                },
                                colors = ButtonDefaults.buttonColors(Color.Transparent)
                            ) {
                                Text(
                                    text = correctCountryNameInGame,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 23.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                when (tiltDirection.value) {
                                    TiltDirection.RIGHT -> {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "¡Correcto!",
                                            Toast.LENGTH_SHORT
                                        ).show()
    
                                        val progressDialog = AlertDialog.Builder(this@PantallaJuego)
                                            .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
                                            .setCancelable(false)
                                            .create()
                                        progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                        progressDialog.show()
    
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            progressDialog.dismiss()
                                            startActivity(intent)
                                            buttonIsVisible = true
                                            capitalVisibility = false
                                        }, 2000)
                                    }

                                    else -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ShowCapital(correctCountryCapitalInGame: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                capitalVisibility,
                enter = scaleIn(
                    initialScale = 0.3f,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            ) {
                Text(
                    text = correctCountryCapitalInGame,
                    fontSize = 22.sp,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.White
                )

            }
            AnimatedVisibility(
                buttonIsVisible, exit = scaleOut(
                    targetScale = 0.4f,
                    animationSpec = TweenSpec(durationMillis = 600)
                )
            ) {
                Button(
                    onClick = {
                        buttonIsVisible = !buttonIsVisible; capitalVisibility = !capitalVisibility
                    },
                    modifier = Modifier.padding(top = 30.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF335ABD))
                ) {
                    Text(text = "Ayuda", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

    @Composable
    fun TopBarQR() {
        TopAppBar(
            title = { Text(text = "", modifier = Modifier, Color.White) },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            this@PantallaJuego,
                            MainActivity::class.java
                        )
                    )
                    finish()
                }
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "icono menu"
                    )
                }
            }
        )
    }
}



