package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaPerfilUsuarioViewModel



class PantallaPerfilUsuario : ComponentActivity() {
    val viewModel : PantallaPerfilUsuarioViewModel by viewModels ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            perfil()
        }
    }

    val CAMERA_REQUEST_CODE = 0
lateinit var image:Bitmap

    /* permisos*/

    /*Esta funcion lo que hace es preguntar si  chekSelPermission es distinto
    * de permiso ok, si es asi entro por el else y abro la camara directamente sino es asi
    * llamo a requestcameraPermision*/
fun pedirPermisoCamara(){

    if(checkSelfPermission(this@PantallaPerfilUsuario,Manifest.permission.CAMERA)
        !=PackageManager.PERMISSION_GRANTED)
    {
        /*Permiso no aceptado por el momento*/
        requestCameraPermiso()
    }else{
        abrirCamara()
    }
}
    /*esta funcion pregunta si los permisos ya han sido rechazados , si es asi , no hace nada porque ya lso rechazo
    * previamente el usuario.
    * si no han sido rechazados nunca , entreo por el else
    * y le pido el permiso necesario, en este caso es la camara*/
    private fun requestCameraPermiso() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this,"si desea poner una foto al perfil , POR FAVOR, agregue los permisos manualmente",Toast.LENGTH_LONG).show()
            /* aca ya rechazo los permisos*/
        }else{
            /*le pido los permisos*/
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }
    }
/*esta funcion lo que hace es sobreescribir el meteodo onRequestPermissionsResult,.


* */
 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Recibo el resultado
    if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
          image = data?.extras?.get("data") as Bitmap
      viewModel.fotoSacadaCamara(image)
    } else {
        Toast.makeText(this,"permiso rechazado por primera vez",Toast.LENGTH_LONG).show()
        /*el permiso no ha sido aceptado*/
    }
}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                
                abrirCamara()

            }else{
                Toast.makeText(this,"permiso rechazado por primera vez",Toast.LENGTH_LONG).show()
                /*el permiso no ha sido aceptado*/
            }
        }
    }
    private fun abrirCamara() {
        val intent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)

        //   Toast.makeText(this, "Abriendo Camara", Toast.LENGTH_LONG).show()

    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun perfil() {
        val nombre:String by viewModel.nombre.observeAsState(initial = "")
        val email:String by viewModel.email.observeAsState(initial = "")
        val nacionalidad:String by viewModel.nacionalidad.observeAsState(initial = "")
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBarPerfil() },
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)) {
                Spacer(modifier = Modifier.padding(14.dp))
                nombreRegistro(Modifier.align(CenterHorizontally), nombre){viewModel.cambiarNombre(it)}
                Spacer(modifier = Modifier.padding(8.dp))
                email(Modifier.align(CenterHorizontally), email){viewModel.cambiarEmail(it)}
                Spacer(modifier = Modifier.padding(8.dp))
                nacionalidad(Modifier.align(CenterHorizontally), nacionalidad){viewModel.cambiarNacionalidad(it)}
                Spacer(modifier = Modifier.padding(8.dp))
                tomarFoto(Modifier.align(CenterHorizontally))
                Spacer(modifier = Modifier.padding(10.dp))
                fotoPerfil(Modifier.align(CenterHorizontally)    .clip(CircleShape))
                Spacer(modifier = Modifier.padding(10.dp))
                botonGuardarCambios(Modifier.align(CenterHorizontally)){viewModel.guardarCambios(nombre,email,nacionalidad)}
            }
        }
    }

    @Composable
    fun nombreRegistro(modifier: Modifier, nombre :String, cambiarNombre: (String) -> Unit ) {
        TextField(value = nombre, onValueChange = {cambiarNombre(it)},
            modifier = modifier.width(300.dp).clip(RoundedCornerShape(50.dp)),
            placeholder = { androidx.compose.material.Text(text = "Nombre") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF0F0F0F),
                backgroundColor = Color(0xFF939599)
            )
        )
    }

    @Composable
    fun email(modifier: Modifier,email :String,cambiarNombre:(String)-> Unit ) {
        TextField(value = email, onValueChange = {cambiarNombre(it)},
            modifier = modifier.width(300.dp).clip(RoundedCornerShape(50.dp)),
            placeholder = { androidx.compose.material.Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF0F0F0F),
                backgroundColor = Color(0xFF939599)
            )
        )
    }

    @Composable
    fun nacionalidad(modifier: Modifier,nacionalidad :String,onTextFieldChanged:(String)-> Unit) {
        TextField(value = nacionalidad, onValueChange = {onTextFieldChanged(it)},
            modifier = modifier.width(300.dp).clip(RoundedCornerShape(50.dp)),
            placeholder = { androidx.compose.material.Text(text = "Pais") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF0F0F0F),
                backgroundColor = Color(0xFF939599)
            )
        )
    }

    @Composable
    fun tomarFoto(modifier: Modifier) {
        Button(modifier = modifier.height(60.dp)
            .width(300.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF939599)),
            onClick = {  pedirPermisoCamara()
            }) {
            androidx.compose.material.Text(text = "Foto perfil (Presione aquí)")
        }
    }
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun fotoPerfil(modifier: Modifier) {
        val fotoBitmap: Bitmap? = viewModel.fotosacadaAhora.value
        val imagenFoto: ImageBitmap? = fotoBitmap?.asImageBitmap()
        Box(modifier = modifier.height(250.dp).width(250.dp)) {
            if (imagenFoto != null) {

                Image(
                    bitmap = imagenFoto, contentDescription = "",
                    modifier = modifier.fillMaxHeight().fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.avatar), contentDescription = "",
                    modifier = modifier.fillMaxHeight().fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun botonGuardarCambios(modifier: Modifier,cambiarNombre:(String)->Unit) {
        Button(modifier = modifier
            .height(50.dp)
            .width(200.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF396AE9)),
            onClick = {
            }) {
            androidx.compose.material.Text(text = "Guardar Cambios")
        }
    }

    @Composable
    fun topBarPerfil(
    ) {
        var showMenu by remember {
            mutableStateOf(false)
        }
        TopAppBar(
            title = { androidx.compose.material.Text(text = "Mi Perfil", modifier = Modifier, Color.White,) },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {   startActivity(
                    Intent(this@PantallaPerfilUsuario,
                    MainActivity::class.java)
                )
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
}







