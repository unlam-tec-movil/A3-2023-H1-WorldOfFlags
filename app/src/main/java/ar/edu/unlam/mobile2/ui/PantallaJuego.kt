package ar.edu.unlam.mobile2.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import ar.edu.unlam.mobile2.R
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@AndroidEntryPoint
class PantallaJuego : ComponentActivity() {

    private val countriesViewModel: CountriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchCountries()
    }

    private fun launchCountries() {
        lifecycleScope.launch {
            countriesViewModel.startGame()
            withContext(Dispatchers.Main) {
                setContent {
                    PrincipalScreen(countries = countriesViewModel)
                }
            }
        }
    }


    @Composable
    fun PrincipalScreen(countries: CountriesViewModel) {
        Column(
	        Modifier
		        .fillMaxSize()
		        .background(Color.Black)) {
            TopBarQR()
            TopBlock(countries)
            Divider(
                color = Color.DarkGray,
                thickness = 5.5.dp,
                modifier = Modifier.padding(top = 25.dp)
            )
            BottomBlock(countries)
            Spacer(modifier = Modifier.padding(65.dp))
            showCapital(countries)
        }
    }

    @Composable
    fun TopBlock(countries: CountriesViewModel) {
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
	                .padding(start = 17.dp, end = 17.dp, top = 20.dp)
	                .fillMaxWidth()
            )
        }
    }
	
	@OptIn(ExperimentalAnimationApi::class)
	@Composable
	fun BottomBlock(countries: CountriesViewModel) {
			when (2) {
				1 -> {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(max = 300.dp)
					){
						Row(
							horizontalArrangement = Arrangement.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 110.dp)
						) {
							Column(Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally){
								// Boton para el país incorrecto
								Button(
									onClick = {
										Toast.makeText(
											this@PantallaJuego,
											"¡Correcto!",
											Toast.LENGTH_SHORT
										).show()
										launchCountries()
									},
									colors = ButtonDefaults.buttonColors(Color.Transparent)
								){
									countries.correctCountryNameInGame.value?.let {
										Text(
											text = it,
											style = MaterialTheme.typography.headlineMedium,
											modifier = Modifier.fillMaxWidth(),
											fontSize = 23.sp,
											color = Color.White,
											textAlign = TextAlign.Center,
											maxLines = 2,
											overflow = TextOverflow.Ellipsis,
										)
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
							Column(Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally){
								// Boton para el país correcto
								Button(
									onClick = {
										Toast.makeText(
											this@PantallaJuego,
											"Incorrecto :(",
											Toast.LENGTH_SHORT
										).show()
										launchCountries()
									},
									colors = ButtonDefaults.buttonColors(Color.Transparent)
								){
									countries.incorrectCountryNameInGame.value?.let {
										Text(
											text = it,
											style = MaterialTheme.typography.headlineMedium,
											modifier = Modifier.fillMaxWidth(),
											fontSize = 23.sp,
											color = Color.White,
											textAlign = TextAlign.Center,
											maxLines = 2,
											overflow = TextOverflow.Ellipsis,
										)
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
					){
					Row(
						horizontalArrangement = Arrangement.Center,
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 110.dp)
					) {
						Column(Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally){
							// Boton para el país incorrecto
							Button(
								onClick = {
									Toast.makeText(
										this@PantallaJuego,
										"Incorrecto :(",
										Toast.LENGTH_SHORT
									).show()
									launchCountries()
								},
								colors = ButtonDefaults.buttonColors(Color.Transparent)
							){
								countries.incorrectCountryNameInGame.value?.let {
								Text(
									text = it,
									style = MaterialTheme.typography.headlineMedium,
									modifier = Modifier.fillMaxWidth(),
									fontSize = 23.sp,
									color = Color.White,
									textAlign = TextAlign.Center,
									maxLines = 2,
									overflow = TextOverflow.Ellipsis,
								)
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
						Column(Modifier.width(150.dp), horizontalAlignment = Alignment.CenterHorizontally){
							// Boton para el país correcto
							Button(
								onClick = {
									Toast.makeText(
										this@PantallaJuego,
										"¡Correcto!",
										Toast.LENGTH_SHORT
									).show()
									launchCountries()
								},
								colors = ButtonDefaults.buttonColors(Color.Transparent)
							){
								countries.correctCountryNameInGame.value?.let {
								Text(
									text = it,
									style = MaterialTheme.typography.headlineMedium,
									modifier = Modifier.fillMaxWidth(),
									fontSize = 23.sp,
									color = Color.White,
									textAlign = TextAlign.Center,
									maxLines = 2,
									overflow = TextOverflow.Ellipsis,
								)
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
    fun showCapital(countries: CountriesViewModel) {

        Column(
            modifier = Modifier
	            .fillMaxSize()
	            .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var capitalvisibility by remember {
                mutableStateOf(false)
            }
            AnimatedVisibility(
                capitalvisibility,
                enter = scaleIn(
                    initialScale = 0.3f,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            ) {
                countries.correctCountryRegionInGame.value?.let {
                    Text(
                        text = it, fontSize = 22.sp,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(top = 10.dp),
                        color = Color.White
                    )
                }
            }
            var isVisible by remember { mutableStateOf(true) }
            AnimatedVisibility(
                isVisible, exit = scaleOut(
                    targetScale = 0.4f,
                    animationSpec = TweenSpec(durationMillis = 600)
                )
            ) {
                Button(
                    onClick = { capitalvisibility = true; isVisible = false },
                    modifier = Modifier.padding(top = 80.dp),
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
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = "icono menu"
                    )
                }
            }
        )
    }
}




