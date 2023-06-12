package ar.edu.unlam.mobile2.ui.ViewModel


import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.model.UsuarioModel


class PantallaPerfilUsuarioViewModel : ViewModel() {


    val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    val _nacionalidad = MutableLiveData<String>()
    val nacionalidad: LiveData<String> = _nacionalidad

    val _fotoPerfil = MutableLiveData<Bitmap>()
    val fotoPerfil: LiveData<Bitmap> = _fotoPerfil
    val fotosacadaAhora = mutableStateOf<Bitmap?>(null)


    val userName=MutableLiveData<String>()
    val emailUser=MutableLiveData<String>()
    val nacionalidadUser=MutableLiveData<String>()
    val imageUser= MutableLiveData<ImageBitmap?>()


    fun fotoSacadaCamara(imagenNueva: Bitmap) {

        _fotoPerfil.value = imagenNueva
        fotosacadaAhora.value = imagenNueva
    }






    fun cambiarNacionalidad(nacionalidad: String) {
        _nacionalidad.value = nacionalidad
    }

    fun cambiarNombre(nombre: String) {
        _nombre.value = nombre
    }

    fun cambiarEmail(email: String) {
        _email.value = email
    }





}