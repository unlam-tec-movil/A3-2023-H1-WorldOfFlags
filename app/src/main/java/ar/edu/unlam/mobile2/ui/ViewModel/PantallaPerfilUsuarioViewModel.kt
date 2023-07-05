package ar.edu.unlam.mobile2.ui.ViewModel


import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


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


    fun fotoSacadaCamara(imagenNueva: Bitmap) {

        _fotoPerfil.value = imagenNueva
        fotosacadaAhora.value = imagenNueva
    }



}