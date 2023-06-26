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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
    private var puntos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vidas = intent.getIntExtra("vidas", 5)
        puntos = intent.getIntExtra("puntos", 0)
        cancelarMovimiento = intent.getBooleanExtra("cancelarMovimiento", false)

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
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .rotate(0F)
        ) {
            TopBar()
            TopBlock(flag, nameUser, nacionalityUser, imageUser)
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
            Spacer(modifier = Modifier.padding(25.dp))
            ExpandableContent()
            Spacer(modifier = Modifier.padding(15.dp))
            ShowCapital(correctCountryCapitalInGame)

        }
    }

    @Composable
    fun imagenAyuda() {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.aiuda),
                contentDescription = "Ayuda",
                modifier = Modifier.size(40.dp)
            )
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
                            painter = painterResource(id = R.drawable.rotacelu),
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
                        painter = painterResource(id = R.drawable.aiuda),
                        contentDescription = "Ayuda",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun optionRotation() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(Color(0xFF02A4A6))

        ) {
            Image(
                painter = painterResource(id = R.drawable.rotacelu),
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
    }

    @Composable
    private fun optionClick() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color.Gray)
                .padding(top = 2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.celu_sin_movimiento),
                contentDescription = "Imagen",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "Presione la opcion correcta",
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
        }
    }

    @SuppressLint("RememberReturnType")
    @Composable
    fun TopBlock(
        flag: String,
        nameUser: String,
        nacionalityUser: String,
        imageUser: ByteArray?
    ) {
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
                //-------------------------------------------------------------------------------------------------------------------------------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 20.dp, top = 7.dp)
                ) {
                    Text(text = nameUser, color = Color.White, fontSize = 17.sp)
                    Text(nacionalityUser, color = Color.White, fontSize = 17.sp)
                }
                //----------------------------------------------------------------------------------------------------------------------------------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 60.dp, top = 7.dp)
                ) {
                    Text(text = "Puntos :$puntos", color = Color.White, fontSize = 17.sp)
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
                                    if (cancelarMovimiento == true) {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "¡Correcto!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        puntos += 10
                                        val progressDialog =
                                            AlertDialog.Builder(this@PantallaJuego)
                                                .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
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
                                                    .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
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
                                    if (cancelarMovimiento == true) {
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
                                            startActivity(
                                                Intent(
                                                    this@PantallaJuego,
                                                    PantallaPrincipal::class.java
                                                )
                                            )
                                        }
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
                                    if (cancelarMovimiento == true) {
                                        Toast.makeText(
                                            this@PantallaJuego,
                                            "¡Correcto!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        puntos += 10
                                        val progressDialog =
                                            AlertDialog.Builder(this@PantallaJuego)
                                                .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
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
                                                    .setView(R.layout.layout_loading) // Reemplaza "dialog_loading" con el layout personalizado para la animación de carga
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
                        buttonIsVisible = !buttonIsVisible; capitalVisibility =
                        !capitalVisibility
                    },
                    modifier = Modifier.padding(top = 25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF335ABD))
                ) {
                    Text(text = "Ayuda", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }

    @Composable
    fun TopBar() {

        TopAppBar(
            title = { Text(text = "", modifier = Modifier, Color.White) },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {
                    this@PantallaJuego.cancelarMovimiento =
                        !this@PantallaJuego.cancelarMovimiento
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

}



