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
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
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
import kotlinx.coroutines.delay
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
    private var errado by mutableStateOf(false)
    private var acertado by mutableStateOf(false)
  
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
            if (vidas == 0){
                GameOverScreen (onAnimationFinished = {
                    startActivity(Intent(this@PantallaJuego, PantallaPrincipal::class.java))
                })
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopBar()
                    Box(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            TopBlock(modifier = Modifier.weight(0.5f), flag, nameUser, nacionalityUser, imageUser)
                            BottomBlock(modifier = Modifier.weight(0.25f), correctCountryNameInGame, incorrectCountryNameInGame, tiltDirection, latitudeCorrectCountryGame, longitudeCorrectCountryGame)
                            Spacer(modifier = Modifier.height(55.dp))
                            ExpandableContent()
                        }
                    }
                    ShowCapital(modifier = Modifier.weight(0.25f), correctCountryCapitalInGame)
                }
                CountryErrorAnimation(Modifier.drawWithContent {
                    drawContent()
                })
                CountryOkAnimation(Modifier.drawWithContent {
                    drawContent()
                })
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ExpandableContent() {
        var expanded by remember { mutableStateOf(false) }

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(
                            400,
                            250
                        )
                    ) with fadeOut(animationSpec = tween(400)) using
                            SizeTransform { initialSize, targetSize ->
                                if (targetState) {
                                    keyframes {
                                        IntSize(
                                            targetSize.width,
                                            initialSize.height
                                        ) at 320
                                        durationMillis = 250
                                    }
                                } else {
                                    keyframes {
                                        IntSize(initialSize.width, targetSize.height) at 320
                                        durationMillis = 250
                                    }
                                }
                            }
                }
            ) { targetExpanded ->
                if (targetExpanded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(Color(0xFF02A4A6))

                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.rotarcelu),
                            contentDescription = "Imagen",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Rote el dispositivo en dirección a la opción correcta, para desactivar esta funcionalidad, apriete el botón que se encuentra en la parte superior.",
                            style = TextStyle(fontSize = 18.sp),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f)
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.boton_ayuda),
                        contentDescription = "Ayuda",
                        modifier = Modifier.size(45.dp)
                    )
                }
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
                                            acertado=true
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
                                                acertado=true
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
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                                errado = true
                                                lifecycleScope.launch {
                                                    delay(2000)
                                                    launchCountries()
                                                }
                                                this@PantallaJuego.vidas -= 1
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
                                                errado = true
                                                lifecycleScope.launch {
                                                    delay(2000)
                                                    launchCountries()
                                                }
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                                this@PantallaJuego.vidas -= 1
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
                                                errado = true
                                                lifecycleScope.launch {
                                                    delay(1500)
                                                    launchCountries()
                                                }
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                                this@PantallaJuego.vidas -= 1
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
                                                errado = true
                                                lifecycleScope.launch {
                                                    delay(2000)
                                                    launchCountries()
                                                }
                                                buttonIsVisible = true
                                                capitalVisibility = false
                                                this@PantallaJuego.vidas -= 1
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
                                            acertado=true
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
                                                acertado=true
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
            title = {Text(text = "", modifier = Modifier, Color(0xFF396AE9))},
            backgroundColor = Color.Transparent,
            actions = {
                IconButton(onClick = {
                    this@PantallaJuego.cancelarMovimiento = !this@PantallaJuego.cancelarMovimiento
                })
                {
                    if (!cancelarMovimiento) {
                        Image(
                            painter = painterResource(id = R.drawable.movimiento_on),
                            contentDescription = "movimiento ok"
                        )
                    } else {
                        Image(
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
    @Composable
    fun GameOverScreen(onAnimationFinished: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_juego),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val scale = remember { Animatable(0f) }
                val opacity = remember { Animatable(0f) }
                
                LaunchedEffect(Unit) {
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
                    )
                    opacity.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                    )
                    delay(2000)
                    onAnimationFinished()
                }
                
                Image(
                    painter = painterResource(R.drawable.game_over),
                    contentDescription = "Game Over Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size((200.dp * scale.value).value.dp)
                )
                
                if (scale.value == 1f) {
                    Text(
                        text = "Puntaje: $puntos",
                        style = TextStyle(fontSize = 24.sp, color = Color.White),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .alpha(opacity.value)
                    )
                }
            }
        }
    }
    
    @Composable
    fun CountryErrorAnimation(modifier:Modifier) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(errado) {
            if (errado) {
                scope.launch {
                    delay(2000)
                    errado = false
                }
            }
        }
        AnimatedVisibility(
            errado,enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            
            ) {
            Box( modifier = Modifier
                .size(450.dp)
                .background(Color.Transparent), BottomCenter ) {
                Column() {
                    Image(painterResource(id = R.drawable.pais_incorrecto),
                        modifier=Modifier.align(CenterHorizontally),
                        contentDescription = "")
                }
            }
        }
        
    }
    
    @Composable
    fun CountryOkAnimation(modifier:Modifier) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(acertado) {
            if (acertado) {
                scope.launch {
                    delay(1500)
                    acertado = false
                }
            }
        }
        AnimatedVisibility(
            acertado,enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
        ) {
            Box( modifier = Modifier
                .size(450.dp)
                .background(Color.Transparent), BottomCenter ) {
                Column() {
                    Image(painterResource(id = R.drawable.pais_correcto),
                        modifier=Modifier.align(CenterHorizontally),
                        contentDescription = "")
                }
            }
        }
    }

}



