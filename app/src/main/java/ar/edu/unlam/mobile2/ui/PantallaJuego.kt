package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PantallaJuego : ComponentActivity() {

    private val countriesViewModel: CountriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lanzarPaises()
    }

    private fun lanzarPaises() {
        lifecycleScope.launch {
            countriesViewModel.startGame()
            CoroutineScope(Dispatchers.Main).launch {
                setContent{


                    gameScreen(countries = countriesViewModel)

                    topBarQR()
                }
            }
        }
    }
    @Composable
    fun topBarQR(
    ) {
        var showMenu by remember {
            mutableStateOf(false)
        }
        TopAppBar(
            title = { Text(text = "", modifier = Modifier, Color.White) },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {   startActivity(Intent(this@PantallaJuego,
                    MainActivity::class.java))
                    finish()}) {
                    Image(painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
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

@SuppressLint("NotConstructor")
@Composable
fun gameScreen(countries : CountriesViewModel) {
    Spacer(modifier = Modifier.padding(28.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.padding(28.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 7.dp)
                .clip(CircleShape)
                .background(color =Color(0xFF335ABD))
        )
        {
            Image(
                painter = painterResource(id = ar.edu.unlam.mobile2.R.drawable.avatar),
                contentDescription = "Foto de perfil del usuario",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start = 14.dp, top = 5.dp, bottom = 5.dp)
                    .size(57.dp)
                    .clip(CircleShape)
            )
            //-------------------------------------------------------------------------------------------------------------------------------------------
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 20.dp, top = 7.dp)
            ) {
                Text(text = "Nombre", color = Color.White, fontSize = 17.sp)
                Text("Nacionalidad", color = Color.White, fontSize = 17.sp)
            }
            //----------------------------------------------------------------------------------------------------------------------------------------------
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 60.dp, top = 7.dp)
            ) {
                Text(text = "Puntos : 0", color = Color.White, fontSize = 17.sp)
                Text("Vidas : 5", color = Color.White, fontSize = 17.sp)
            }
            //----------------------------------------------------------------------------------------------------------------------------------------------
        }
//-------------------------------------------------------------------------------------------------------------------------------------------------------
        AsyncImage(
          model = countries.correctCountryFlagInGame.value,
          contentDescription = "Bandera",
          modifier = Modifier
              .size(300.dp, 170.dp)
              .padding(start = 17.dp, end = 17.dp, top = 10.dp)
              .fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Divider(
            color = Color.DarkGray,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 2.dp)
        )

        Text(
            text = "Capital", fontSize = 14.sp,
            color = Color.White
        )
        Divider(
            color = Color.DarkGray,
            thickness = 2.dp,
            modifier = Modifier.padding(vertical = 2.dp)
        )
//-----------------------------------------------------------------------------------------------------------------------------------------------------
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            countries.correctCountryNameInGame.value?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .width(140.dp),
                    fontSize = 23.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,

                    )
            }
            Divider(
                Modifier
                    .width(120.dp)
                    .size(height = 50.dp, width = 1.dp)
                    .rotate(90f)
                    .padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color.Gray,
                thickness = 1.dp,
            )

            countries.incorrectCountryNameInGame.value?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .width(155.dp),
                    fontSize = 23.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 80.dp/*, start = 260.dp*/),
            colors = ButtonDefaults.buttonColors(Color(0xFF335ABD))
            // colors = ButtonDefaults.buttonColors(Color.Blue)
        ) {
            Text(text = "Ayuda", style = MaterialTheme.typography.bodyMedium)
        }

    }

    }
}