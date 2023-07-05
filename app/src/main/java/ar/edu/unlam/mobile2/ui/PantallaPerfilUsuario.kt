package ar.edu.unlam.mobile2.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.data.Database.toDatabase
import ar.edu.unlam.mobile2.model.UserModel
import ar.edu.unlam.mobile2.ui.ViewModel.PantallaPerfilUsuarioViewModel
import ar.edu.unlam.mobile2.ui.ViewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class PantallaPerfilUsuario : ComponentActivity() {
    private val viewModel: PantallaPerfilUsuarioViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val nombre: String by viewModel.nombre.observeAsState(initial = "")
            val email: String by viewModel.email.observeAsState(initial = "")
            val nacionalidad: String by viewModel.nacionalidad.observeAsState(initial = "")
            val scaffoldState = rememberScaffoldState()


            val fotoBitmap: Bitmap? = viewModel.fotosacadaAhora.value
            val imagenFoto: ImageBitmap? = fotoBitmap?.asImageBitmap()

            perfil(nombre, email, nacionalidad, scaffoldState, imagenFoto)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun perfil(
        nombre: String,
        email: String,
        nacionalidad: String,
        scaffoldState: ScaffoldState,
        imagenFoto: ImageBitmap?
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { topBarPerfil() },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_qr),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(14.dp))
                    }
                    nombreRegistro(
                        Modifier.align(CenterHorizontally),
                        nombre
                    )
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    email(Modifier.align(CenterHorizontally), email)
                
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    nacionalidad(
                        Modifier.align(CenterHorizontally),
                        nacionalidad
                    )
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                    if (userViewModel.getAllUserDatabase().isEmpty()) {
                        tomarFoto(Modifier.align(CenterHorizontally))
                    }
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    fotoPerfil(
                        imagenFoto,
                        Modifier
                            .align(CenterHorizontally)
                            .clip(CircleShape)
                    )
                    if (userViewModel.getAllUserDatabase().isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(25.dp))
                    } else {
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                    if (userViewModel.getAllUserDatabase().isEmpty()) {
                        botonGuardarCambios(
                            Modifier.align(CenterHorizontally),
                            nombre,
                            email,
                            nacionalidad,
                            imagenFoto
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun nombreRegistro(modifier: Modifier, nombre: String) {
        if (userViewModel.getAllUserDatabase().isEmpty()) {
            TextField(
                value = nombre, onValueChange = { viewModel._nombre.value = it },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,

                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )

            )

        } else {
            userViewModel.getUserDatabase()
            TextField(
                value = userViewModel.userName.value.toString(), onValueChange = {
                    viewModel._nombre.value = it
                },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Nombre") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,

                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )
            )
        }

    }

    @Composable
    fun email(modifier: Modifier, email: String) {
        if (userViewModel.getAllUserDatabase().isEmpty()) {
            TextField(
                value = email, onValueChange = { viewModel._email.value = it },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,

                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )
            )
        } else {
            userViewModel.getUserDatabase()
            TextField(
                value = userViewModel.emailUser.value.toString(), onValueChange = {
                    viewModel._email.value = it
                },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )
            )
        }
    }

    @Composable
    fun nacionalidad(
        modifier: Modifier,
        nacionalidad: String,

        ) {
        if (userViewModel.getAllUserDatabase().isEmpty()) {
            TextField(
                value = nacionalidad, onValueChange = { viewModel._nacionalidad.value = it },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Nacionalidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,

                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )

            )
        } else {
            userViewModel.getUserDatabase()
            TextField(
                value = userViewModel.nacionalityUser.value.toString(), onValueChange = {
                    viewModel._email.value = it
                },
                modifier = modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(50.dp)),
                label = { Text("Nacionalidad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0F0F0F),
                    backgroundColor = Color(0xFF939599)
                )
            )
        }
    }

    @Composable
    fun tomarFoto(modifier: Modifier) {
        Button(modifier = modifier
            .height(60.dp)
            .width(300.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF939599)),
            onClick = {
                askPermission()
             //  pedirPermisoCamara()
            }) {
           Text(text = "Foto perfil (Presione aquí)")
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun fotoPerfil(imagenFoto: ImageBitmap?, modifier: Modifier) {

        if (userViewModel.getAllUserDatabase().isEmpty()) {
            Box(
                modifier = modifier
                    .height(250.dp)
                    .width(250.dp)
            ) {
                if (imagenFoto != null) {

                    Image(
                        bitmap = imagenFoto, contentDescription = "",
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "IMAGEN POR DEFECTO",
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    )
                }
            }
        } else {
            Box(
                modifier = modifier
                    .height(250.dp)
                    .width(250.dp)
            ) {
                val userImage = userViewModel.imageUser.value

                val bitmap = remember {
                    userImage?.let {
                        BitmapFactory.decodeByteArray(
                            userImage, 0,
                            it.size
                        )
                    }
                }
                val imageBitmap = remember { bitmap?.asImageBitmap() }

                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap, contentDescription = "IMAGEN DEL USUARIO",
                        modifier = modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Composable
    fun botonGuardarCambios(
        modifier: Modifier,
        nombre: String,
        email: String,
        nacionalidad: String,
        imagenFoto: ImageBitmap?
    ) {
        val imageInt: Int = R.drawable.avatar
        val imageByte: ByteArray = ByteBuffer.allocate(4).putInt(imageInt).array()
        val bitmap = imagenFoto?.asAndroidBitmap()
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imagenData: ByteArray = stream.toByteArray()

        Button(modifier = modifier
            .height(50.dp)
            .width(200.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF396AE9)),
            onClick = {
                if (imagenData.size > 0) {
                    val user = UserModel(nombre, email, nacionalidad, imagenData)

                    //Guardo en la base de datos
                    userViewModel.setUserDatabase(user.toDatabase())
                } else {
                    val user = UserModel(nombre, email, nacionalidad, imageByte)

                    //Guardo en la base de datos
                    userViewModel.setUserDatabase(user.toDatabase())
                }
                //Guardo en el repositorio local
                //     UserRepository.setUser(user)
                Toast.makeText(
                    this,
                    "Usuario guardado Correctamente",
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this@PantallaPerfilUsuario, PantallaPrincipal::class.java))
            }
        ) {
            Text(text = "Guardar Cambios")
        }
    }

    @Composable
    fun topBarPerfil(
    ) {
        var showMenu by remember {
            mutableStateOf(false)
        }
        TopAppBar(
            title = {
                Text(
                    text = "Mi Perfil",
                    modifier = Modifier,
                    Color.White,
                )
            },
            backgroundColor = Color.Black,
            actions = {
                IconButton(onClick = {
                    startActivity(
                        Intent(
                            this@PantallaPerfilUsuario,
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
                DropdownMenu(
                    expanded = showMenu, onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .width(110.dp)
                        .background(color = Color(0xFF335ABD)),
                )
                {
                }
            }
        )
    }
    lateinit var image: Bitmap



    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val photo: Bitmap? = data?.extras?.get("data") as Bitmap?
            if (photo != null) {
                image = photo
                viewModel.fotoSacadaCamara(image)
            }
        } else {
            Toast.makeText(this, "permiso rechazado por primera vez", Toast.LENGTH_LONG).show()

        }
    }

    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       // intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT)
    takePictureLauncher.launch(intent)
        //startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
               abrirCamara()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de la cámara ha sido denegado." +
                            " No se puede capturar la foto.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun askPermission() = when {
       checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            abrirCamara()
        }
        else -> {

            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}







