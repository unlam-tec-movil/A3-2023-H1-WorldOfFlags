package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Intent

import androidx.compose.foundation.background
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

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

    @SuppressLint(
        "UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState",
        "UnusedMaterialScaffoldPaddingParameter"
    )
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun pantallaInicio() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        )
        {
            Image(
                painter = painterResource(id = R.drawable.fondo_seleccion),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 150.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            startActivity(
                                Intent(
                                    this@PantallaPrincipal,
                                    PantallaJuego::class.java
                                )
                            )
                            finish()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.solitario),
                            contentDescription = "imagenPista",
                            modifier = Modifier
                                .size(120.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                
                    Button(
                        onClick = {
                            startActivity(
                                Intent(
                                    this@PantallaPrincipal,
                                    PantallaVersus::class.java
                                )
                            )
                            finish()
                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.versus),
                            contentDescription = "imagenPista",
                            modifier = Modifier
                                .size(120.dp),
                        )
                    }
                }
            }
        }
    }
}
