package ar.edu.unlam.mobile2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile2.R

class PantallaFinJuegoVersus : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val puntos =  intent.getIntExtra("puntos", 0)
        val paisesAcertados =intent.getIntExtra("paisesAcertados",0)
        setContent {

       PantallaFinal(puntos = puntos,paisesAcertados)
        }
    }



    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun PantallaFinal(puntos: Int, paisesAcertados: Int){

        intent.putExtra("puntos", puntos)
        intent.putExtra("paisesAcertados",paisesAcertados)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                ImagenLogo()
                Spacer(modifier = Modifier.padding(20.dp))
                        Text(
                            text = " Su Puntaje  es: $puntos  puntos    ",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(CenterHorizontally),
                            softWrap = true,
                            fontSize = 25.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = " Acertó $paisesAcertados  pais(es)",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(CenterHorizontally),
                            softWrap = true,
                            fontSize = 25.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                        )

                Spacer(modifier = Modifier.padding(10
                    .dp))
                boton(Modifier.align(CenterHorizontally))
            }
    }

    @Composable
    fun boton(modifier: Modifier) {
        Button(
            modifier = modifier
                .height(50.dp)
                .width(180.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(Color(0xFF396AE9)),

            onClick = {
                startActivity(Intent(this@PantallaFinJuegoVersus, PantallaPrincipal::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish()
            }) {
            Text(text = "Volver")

        }
    }
    @Composable
    fun ImagenLogo() {
        Image(
            painter = painterResource(id = R.drawable.mundo),
            contentDescription = "imagen logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
        )
    }
}