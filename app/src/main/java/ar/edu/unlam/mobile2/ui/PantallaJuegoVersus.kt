package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.model.CountryModel
import ar.edu.unlam.mobile2.model.DatosJuego
import ar.edu.unlam.mobile2.movimiento.DetectarMovimiento
import ar.edu.unlam.mobile2.movimiento.TiltDirection
import ar.edu.unlam.mobile2.ui.ViewModel.CountriesViewModel
import ar.edu.unlam.mobile2.ui.ViewModel.UserViewModel
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import kotlin.random.Random

@AndroidEntryPoint
class PantallaJuegoVersus : ComponentActivity() {
    
    private lateinit var motionDetector: DetectarMovimiento
    private lateinit var countriesQR: List<CountryModel>
    private val countriesViewModel: CountriesViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var countryIndex: Int = 0
    private var puntos :Int =0
    private var paisesAcertados: Int =0
    private var cancelarMovimiento by mutableStateOf(true)
    private var errado by mutableStateOf(false)
    private var acertado by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        motionDetector = DetectarMovimiento(this)
        motionDetector.start()
        countryIndex = intent.getIntExtra("index", 0)
        countriesQR = DatosJuego.listaPaises
        puntos = intent.getIntExtra("puntos", 0)
        paisesAcertados = intent.getIntExtra("paisesAcertados",0)
        cancelarMovimiento = intent.getBooleanExtra("cancelarMovimiento",false)
            launchCountries()
    }
    private var terminoElJuego by mutableStateOf(false)
    @OptIn(ExperimentalAnimationApi::class)
    private fun launchCountries(){
        
        if (countryIndex != countriesQR.size) {
            val progressDialog = AlertDialog.Builder(this@PantallaJuegoVersus)
                .setView(R.layout.layout_loading)
                .setCancelable(false)
                .create()
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            progressDialog.show()
            val country = countriesQR[countryIndex]
            lifecycleScope.launch {
                countriesViewModel.startGameQR(country)
                countryIndex += 1
                withContext(Dispatchers.Main){
                    progressDialog.dismiss()
                    setContent {
                        val flag = countriesViewModel.correctCountryFlagInGame.value
                        val correctCountryNameInGame = countriesViewModel.correctCountryNameInGame.value
                        val incorrectCountryNameInGame = countriesViewModel.incorrectCountryNameInGame.value
                        val correctCountryCapitalInGame = countriesViewModel.correctCountryCapitalInGame.value
                        val tiltDirection = motionDetector.tiltDirection.collectAsState()
                        val latitudeCorrectCountryGame = countriesViewModel.latitudeCorrectCountryGame.value
                        val longitudeCorrectCountryGame = countriesViewModel.longitudeCorrectCountryGame.value

                        userViewModel.getUserDatabase()


                        val nameUser = userViewModel.userName.value
                        val nationalityUser = userViewModel.nacionalityUser.value
                        val imagenUser = userViewModel.imageUser.value

                        if (flag != null && correctCountryNameInGame != null && incorrectCountryNameInGame != null && correctCountryCapitalInGame != null && latitudeCorrectCountryGame != null && longitudeCorrectCountryGame != null) {
                            PrincipalScreen(

                                flag,
                                correctCountryNameInGame,
                                incorrectCountryNameInGame,
                                correctCountryCapitalInGame,
                                tiltDirection,
                                latitudeCorrectCountryGame,
                                longitudeCorrectCountryGame,
                                nameUser,
                                nationalityUser,
                                imagenUser
                            )
                        }
                    }
                }
            }
        } else {


                val intent = Intent(this, PantallaFinJuegoVersus::class.java)
                intent.putExtra("paisesAcertados", paisesAcertados)
                intent.putExtra("puntos", puntos)
               startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

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
        nameUser: String?,
        nationalityUser: String?,
        imagenUser: ByteArray?
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .rotate(0F)
        ) {
            TopBar()
            if (nameUser != null && nationalityUser != null) {
                TopBlock(flag, nameUser, nationalityUser, imagenUser)
            }
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
        CountriErrorAnimation(Modifier.drawWithContent {
            drawContent()
        })
        CountriOkAnimation(Modifier.drawWithContent {
            drawContent()
        })
    }

    @Composable
    fun TopBlock(flag: String, nameUser: String, nationalityUser: String, imagenUser: ByteArray?) {
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
                    imagenUser?.let {
                        BitmapFactory.decodeByteArray(
                            imagenUser, 0,
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
                }else{
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
                        Text(text = nationalityUser, color = Color.White, fontSize = 17.sp)

                }
                //----------------------------------------------------------------------------------------------------------------------------------------------
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 60.dp, top = 7.dp)
                ) {
                    Text(text = "Puntos :$puntos", color = Color.White, fontSize = 17.sp)

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
        longitudeCorrectCountryGame: Double,
    ) {
        val intent = Intent(this, PantallaMapa::class.java)
        intent.putExtra("latitude", latitudeCorrectCountryGame)
        intent.putExtra("longitude", longitudeCorrectCountryGame)
        intent.putExtra("versus", true)
        intent.putExtra("index", countryIndex)

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
                                onClick = {if(cancelarMovimiento == true) {
                                   acertado=true
                                    paisesAcertados+=1
                                    puntos+=10
                                    intent.putExtra("paisesAcertados",paisesAcertados)
                                    intent.putExtra("puntos", puntos)
                                    lifecycleScope.launch {
                                        delay(2000)
                                        startActivity(intent)
                                    }
                                    buttonIsVisible = true
                                    capitalVisibility = false
                                }},
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
                                if(cancelarMovimiento == false) {
                                    when (tiltDirection.value) {
                                        TiltDirection.LEFT -> {
                                            acertado=true
                                            paisesAcertados+=1
                                            puntos+=10
                                            intent.putExtra("paisesAcertados",paisesAcertados)
                                            intent.putExtra("puntos", puntos)
                                            lifecycleScope.launch {
                                                delay(2000)
                                                startActivity(intent)
                                            }
                                            buttonIsVisible = true
                                            capitalVisibility = false
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
                                onClick = {if(cancelarMovimiento == true) {
                                    errado = true
                                    lifecycleScope.launch {
                                        delay(2000)
                                        launchCountries()
                                    }
                                    buttonIsVisible = true
                                    capitalVisibility = false
                                }},
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
                                if(cancelarMovimiento == false){
                                when (tiltDirection.value) {
                                    TiltDirection.RIGHT -> {
                                        errado = true
                                        lifecycleScope.launch {
                                            delay(2000)
                                            launchCountries()
                                        }
                                        buttonIsVisible = true
                                        capitalVisibility = false
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
                                onClick = {if(cancelarMovimiento == true) {
                                    errado = true
                                    lifecycleScope.launch {
                                        delay(2000)
                                        launchCountries()
                                    }
                                    buttonIsVisible = true
                                    capitalVisibility = false
                                } },
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
                                if(cancelarMovimiento == false) {
                                    when (tiltDirection.value) {
                                        TiltDirection.LEFT -> {
                                            errado = true
                                            lifecycleScope.launch {
                                                delay(2000)
                                                launchCountries()
                                            }
                                            buttonIsVisible = true
                                            capitalVisibility = false
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
                                onClick = {if(cancelarMovimiento == true) {
                                    acertado=true
                                    paisesAcertados+=1
                                    puntos+=10
                                    intent.putExtra("paisesAcertados",paisesAcertados)
                                    intent.putExtra("puntos", puntos)
                                    lifecycleScope.launch {
                                        delay(2000)
                                        startActivity(intent)
                                    }
                                    buttonIsVisible = true
                                    capitalVisibility = false
                                }},
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
                                if(cancelarMovimiento == false){
                                when (tiltDirection.value) {
                                    TiltDirection.RIGHT -> {
                                        acertado=true
                                        paisesAcertados+=1
                                        puntos+=10
                                        intent.putExtra("paisesAcertados",paisesAcertados)
                                        intent.putExtra("puntos", puntos)
                                        lifecycleScope.launch {
                                            delay(2000)
                                            startActivity(intent)
                                        }
                                        buttonIsVisible = true
                                        capitalVisibility = false
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
    fun CountriErrorAnimation(modifier:Modifier) {
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
                .background(Color.Transparent), Alignment.BottomCenter
            ) {
                Column() {
                    Image(painterResource(id = R.drawable.paisenojado1),
                        modifier=Modifier.align(Alignment.CenterHorizontally),
                        contentDescription = "")
                }

            }
        }

    }
    @Composable
    fun CountriOkAnimation(modifier:Modifier) {
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
                .background(Color.Transparent), Alignment.BottomCenter
            ) {
                Column() {
                    Image(painterResource(id = R.drawable.paiscontento),
                        modifier=Modifier.align(Alignment.CenterHorizontally),
                        contentDescription = "")
                }
            }
        }
    }
    @Composable
    fun TopBar() {

        TopAppBar(
            title = {
                    Text(text = "", modifier = Modifier,Color(0xFF396AE9 ))},
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = { this@PantallaJuegoVersus.cancelarMovimiento =! this@PantallaJuegoVersus.cancelarMovimiento})
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
                Spacer(modifier = Modifier.width(120.dp))
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            this@PantallaJuegoVersus,
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



