package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.movimiento.DetectarMovimiento
import ar.edu.unlam.mobile2.movimiento.TiltDirection
import ar.edu.unlam.mobile2.ui.ViewModel.CountriesViewModel
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaPerfilUsuarioViewModel
import ar.edu.unlam.mobile2.ui.ViewModel.UserViewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@AndroidEntryPoint
class PantallaJuego : ComponentActivity() {
    private lateinit var motionDetector: DetectarMovimiento
    private val countriesViewModel: CountriesViewModel by viewModels()
    private val viewModel: PantallaPerfilUsuarioViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()


    private var cancelarMovimiento by mutableStateOf(true)
    private var vidas: Int = 0
    private var puntos :Int =0
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        vidas = intent.getIntExtra("vidas", 5)
        puntos = intent.getIntExtra("puntos", 0)
        cancelarMovimiento = intent.getBooleanExtra("cancelarMovimiento",false)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        motionDetector = DetectarMovimiento(this)
        motionDetector.start()
        launchCountries()


    }

    @SuppressLint("CoroutineCreationDuringComposition")
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


                    userViewModel.getUserDatabase()


                    val nameUser = userViewModel.userName.value
                    val nationalityUser = userViewModel.nacionalityUser.value
                    val imagenUser = userViewModel.imageUser.value



                    if (nameUser != null && nationalityUser != null && flag != null && correctCountryNameInGame != null && incorrectCountryNameInGame != null && correctCountryCapitalInGame != null && latitudeCorrectCountryGame != null && longitudeCorrectCountryGame != null) {

                    
                        PrincipalScreen(
                            nameUser,
                            nationalityUser,
                            imagenUser,
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
        nameUser: String,
        nacionalityUser: String,
        imageUser: ByteArray?,
        flag: String,
        correctCountryNameInGame: String,
        incorrectCountryNameInGame: String,
        correctCountryCapitalInGame: String,
        tiltDirection: State<TiltDirection>,
        latitudeCorrectCountryGame: Double,
        longitudeCorrectCountryGame: Double,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_juego),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(modifier = Modifier.fillMaxSize()) {
                TopBar()
                Box(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TopBlock(modifier = Modifier.weight(0.5f), flag, nameUser, nacionalityUser, imageUser)
                        BottomBlock(modifier = Modifier.weight(0.25f), correctCountryNameInGame, incorrectCountryNameInGame, tiltDirection, latitudeCorrectCountryGame, longitudeCorrectCountryGame)
                    }
                }
                ShowCapital(modifier = Modifier.weight(0.25f), correctCountryCapitalInGame)
            }
        }
    }

    @SuppressLint("RememberReturnType")
    @Composable
    fun TopBlock(modifier: Modifier = Modifier, flag: String, nameUser: String, nacionalityUser: String, imageUser: ByteArray?) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .heightIn(min = 0.dp, max = 200.dp)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = Color(0xFF02A4A6))
                    .border(BorderStroke(2.dp, Color(0xFF105590)))
                    .align(Alignment.TopStart)
            ) {
                val bitmap = remember {
                    imageUser?.let {
                        BitmapFactory.decodeByteArray(
                            imageUser, 0,
                            it.size
                        )
                    }
                }
                val imageBitmap = remember { bitmap?.asImageBitmap() }
            
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Foto de perfil del usuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 14.dp, top = 5.dp, bottom = 5.dp)
                            .size(57.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painterResource(id = R.drawable.avatar),
                        contentDescription = "Foto de perfil del usuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 14.dp, top = 5.dp, bottom = 5.dp)
                            .size(57.dp)
                            .clip(CircleShape)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 20.dp, top = 4.dp)
                ) {
                    Text(text = nameUser, color = Color(0xFF105590), fontSize = 17.sp)
                    Text(text = nacionalityUser, color = Color(0xFF105590), fontSize = 17.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 80.dp, end = 8.dp, top = 4.dp)
                ) {
                    Text(text = "Puntos: $puntos", color = Color(0xFF105590), fontSize = 17.sp)
                    Text(text = "Vidas: $vidas", color = Color(0xFF105590), fontSize = 17.sp)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿A qué país corresponde ésta bandera?",
                    color = Color(0xFF02A4A6),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .align(Alignment.BottomCenter)
            ) {
                AsyncImage(
                    model = flag,
                    contentDescription = "Bandera",
                    modifier = Modifier
                        .size(150.dp)
                        .weight(0.5f)
                        .clipToBounds()
                )
            }
        }
    }

    private var buttonIsVisible by mutableStateOf(true)
    private var capitalVisibility by mutableStateOf(false)

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun BottomBlock(
        modifier: Modifier = Modifier,
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
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .width(170.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, Color(0xFF105590)),
                                        shape = RectangleShape
                                    )
                                    .background(Color(0xFF02A4A6))
                            ) {
                                // Boton para el país correcto
                                Button(
                                    onClick = {
                                        if (cancelarMovimiento == true) {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "¡Correcto!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            puntos += 10
                                            val progressDialog =
                                                AlertDialog.Builder(this@PantallaJuego)
                                                    .setView(R.layout.layout_loading)
                                                    .setCancelable(false)
                                                    .create()
                                            progressDialog.window?.setBackgroundDrawable(
                                                ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT
                                                )
                                            )
                                            progressDialog.show()
                                    
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                progressDialog.dismiss()
                                                intent.putExtra("puntos", puntos)
                                                intent.putExtra("cancelarMovimiento", cancelarMovimiento)
                                                startActivity(intent)
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                            }, 2000)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = correctCountryNameInGame,
                                        color = Color(0xFF105590),
                                        textAlign = TextAlign.Center,
                                    )
                                    if (cancelarMovimiento == false) {
                                
                                        when (tiltDirection.value) {
                                            TiltDirection.LEFT -> {
                                                Toast.makeText(
                                                    this@PantallaJuego,
                                                    "¡Correcto!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                puntos += 10
                                                val progressDialog =
                                                    AlertDialog.Builder(this@PantallaJuego)
                                                        .setView(R.layout.layout_loading)
                                                        .setCancelable(false)
                                                        .create()
                                                progressDialog.window?.setBackgroundDrawable(
                                                    ColorDrawable(android.graphics.Color.TRANSPARENT)
                                                )
                                        
                                                progressDialog.show()
                                        
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    progressDialog.dismiss()
                                                    intent.putExtra("puntos", puntos)
                                                    intent.putExtra(
                                                        "cancelarMovimiento",
                                                        cancelarMovimiento
                                                    )
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
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier
                                .width(170.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, Color(0xFF105590)),
                                        shape = RectangleShape
                                    )
                                    .background(Color(0xFF02A4A6))
                            ) {
                                // Boton para el país correcto
                                Button(
                                    onClick = {
                                        if (cancelarMovimiento == true) {
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
                                                startActivity(
                                                    Intent(
                                                        this@PantallaJuego,
                                                        PantallaPrincipal::class.java
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = incorrectCountryNameInGame,
                                        color = Color(0xFF105590),
                                        textAlign = TextAlign.Center,
                                    )
                                    if (cancelarMovimiento == false) {
                                
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
                                                    startActivity(
                                                        Intent(
                                                            this@PantallaJuego,
                                                            PantallaPrincipal::class.java
                                                        )
                                                    )
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
                }
            }
    
            2 -> {
                Box(
                    modifier = modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .width(170.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, Color(0xFF105590)),
                                        shape = RectangleShape
                                    )
                                    .background(Color(0xFF02A4A6))
                            ) {
                                // Boton para el país correcto
                                Button(
                                    onClick = {
                                        if (cancelarMovimiento == true) {
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
                                                startActivity(
                                                    Intent(
                                                        this@PantallaJuego,
                                                        PantallaPrincipal::class.java
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = incorrectCountryNameInGame,
                                        color = Color(0xFF105590),
                                        textAlign = TextAlign.Center,
                                    )
                                    if (cancelarMovimiento == false) {
                                
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
                                                    startActivity(
                                                        Intent(
                                                            this@PantallaJuego,
                                                            PantallaPrincipal::class.java
                                                        )
                                                    )
                                                }
                                            }
                                    
                                            else -> {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier
                                .width(170.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(2.dp, Color(0xFF105590)),
                                        shape = RectangleShape
                                    )
                                    .background(Color(0xFF02A4A6))
                            ) {
                                // Boton para el país correcto
                                Button(
                                    onClick = {
                                        if (cancelarMovimiento == true) {
                                            Toast.makeText(
                                                this@PantallaJuego,
                                                "¡Correcto!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            puntos += 10
                                            val progressDialog =
                                                AlertDialog.Builder(this@PantallaJuego)
                                                    .setView(R.layout.layout_loading)
                                                    .setCancelable(false)
                                                    .create()
                                            progressDialog.window?.setBackgroundDrawable(
                                                ColorDrawable(
                                                    android.graphics.Color.TRANSPARENT
                                                )
                                            )
                                    
                                            progressDialog.show()
                                    
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                progressDialog.dismiss()
                                                intent.putExtra("puntos", puntos)
                                                intent.putExtra(
                                                    "cancelarMovimiento",
                                                    cancelarMovimiento
                                                )
                                                startActivity(intent)
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                            }, 2000)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    Text(
                                        text = correctCountryNameInGame,
                                        color = Color(0xFF105590),
                                        textAlign = TextAlign.Center,
                                    )
                                    if (cancelarMovimiento == false) {
                                
                                        when (tiltDirection.value) {
                                            TiltDirection.RIGHT -> {
                                                Toast.makeText(
                                                    this@PantallaJuego,
                                                    "¡Correcto!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                puntos += 10
                                                val progressDialog =
                                                    AlertDialog.Builder(this@PantallaJuego)
                                                        .setView(R.layout.layout_loading)
                                                        .setCancelable(false)
                                                        .create()
                                                progressDialog.window?.setBackgroundDrawable(
                                                    ColorDrawable(android.graphics.Color.TRANSPARENT)
                                                )
                                        
                                                progressDialog.show()
                                        
                                                Handler(Looper.getMainLooper()).postDelayed({
                                                    progressDialog.dismiss()
                                                    intent.putExtra("puntos", puntos)
                                                    intent.putExtra(
                                                        "cancelarMovimiento",
                                                        cancelarMovimiento
                                                    )
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
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ShowCapital(modifier: Modifier = Modifier, correctCountryCapitalInGame: String) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
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
                    text = "La capital es: $correctCountryCapitalInGame",
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
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pista),
                        contentDescription = "imagenPista",
                        modifier = Modifier
                            .size(150.dp)
                    )
                }
            }
        }
    }
    @Composable
    fun TopBar() {

        TopAppBar(
            title = { Text(text = "", modifier = Modifier, Color.White) },
            backgroundColor = Color.Transparent,
            actions = {
                IconButton(onClick = { this@PantallaJuego.cancelarMovimiento =! this@PantallaJuego.cancelarMovimiento})
                {
                    if(!cancelarMovimiento) {
                        Image(
                            painter = painterResource(id = R.drawable.movimiento_on),
                            contentDescription = "movimiento ok"
                        )
                    }else{ Image(
                        painter = painterResource(id = R.drawable.movimento_of),
                        contentDescription = "movimiento of"
                    )
                    }
                }
                Spacer(modifier = Modifier.width(130.dp))
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



