package ar.edu.unlam.mobile2.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile2.R


class PantallaCrearUsuario : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
CartelInicio()

        }
    }

    @Composable
    fun ImagenLogo() {
        Image(
            painter = painterResource(id = R.drawable.mundo),
            contentDescription = "imagen logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
    }

@Preview
    @Composable
    fun CartelInicio(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    {
        Box(
            Modifier.align(Alignment.CenterHorizontally)

                .heightIn(min = 100.dp, max = 100.dp)
        ) {
            Text(
                text = " Bienvenido a ",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                softWrap = true,
                fontSize = 25.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
        }
        ImagenLogo()
        Spacer(modifier = Modifier.padding(20.dp))
        Box(
            Modifier.align(Alignment.CenterHorizontally)

                .heightIn(min = 100.dp, max = 150.dp)
        ) {
            Text(
                text = " POR FAVOR                " +
                        "             Registre un  usuario                 " +
                        "    para poder de Comenzar",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                softWrap = true,
                fontSize = 25.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.padding(28.dp))
boton(Modifier.align(Alignment.CenterHorizontally))
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
                startActivity(Intent(this@PantallaCrearUsuario, PantallaPerfilUsuario::class.java))
                finish()
            }) {
            Text(text = "Aceptar")

        }
    }
}

