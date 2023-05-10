package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Intent

import androidx.compose.foundation.background
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.*
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp

import ar.edu.unlam.mobile2.R


@Suppress("CAST_NEVER_SUCCEEDS")
class PantallaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
pantallaInicio()

        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun pantallaInicio() {
        val scaffoldState = rememberScaffoldState()
        //val scaffoldState = rememberUpdatedState(mutableStateOf(false) )
        Scaffold(

            scaffoldState = scaffoldState,
           topBar = { topBar() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
            {
              //  topBar()
                imagenLogo()

                Spacer(modifier = Modifier.padding(28.dp))
                partidaSolitaria(Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.padding(20.dp))
                partidaVersus(Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun topBar() {

            var showMenu by remember {
                mutableStateOf(false)
            }
            TopAppBar(
                title = { Text(text = "", modifier = Modifier, Color.White) },
           colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color(0xFF070707)),
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
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
                        DropdownMenuItem(onClick = {
                            startActivity(Intent(this@PantallaPrincipal,PantallaPerfilUsuario::class.java))
                        },
                            text = {Text(text = "Perfil",
                                color = Color(0xFF070707)) },

                            leadingIcon = {
                                Icon( imageVector = Icons.Filled.Person,
                                    contentDescription = "volver")
                            }
                        )
                        DropdownMenuItem(onClick = { finish() },
                            text = {Text( text = "Salir",
                                color = Color(0xFF070707))},
                            leadingIcon = { Icon(imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "volver")

                            }
                                    )
                    }
                }

                    )
        }


    @Composable
    fun partidaSolitaria(modifier: Modifier) {
        Button(
            modifier = modifier
                .height(50.dp)
                .width(180.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(  Color(0xFF396AE9)),

            onClick = {
                startActivity(Intent(this@PantallaPrincipal, PantallaJuego::class.java))
                finish()
            }) {
            Text(text = "Solitario")

        }
    }

    @Composable
    fun partidaVersus(modifier: Modifier) {
        Button(
            modifier = modifier
                .height(50.dp)
                .width(180.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors( Color(0xFF396AE9)),


            onClick = {
                startActivity(Intent(this@PantallaPrincipal, PantallaVersus::class.java))
                finish()
            }) {
            Text(text = "Versus")

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

}
