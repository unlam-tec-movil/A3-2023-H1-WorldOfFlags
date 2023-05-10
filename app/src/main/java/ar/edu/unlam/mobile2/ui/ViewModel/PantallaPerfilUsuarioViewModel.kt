package ar.edu.unlam.mobile2.ui.ViewModel



import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PantallaPerfilUsuarioViewModel: ViewModel() {


    private val _nombre = MutableLiveData<String>()
    val nombre :LiveData<String> = _nombre

    private val _email = MutableLiveData<String>()
    val email :LiveData<String> = _email
    private val _nacionalidad = MutableLiveData<String>()
    val nacionalidad :LiveData<String> = _nacionalidad

    private val _fotoPerfil = MutableLiveData<Bitmap>()
   val fotoPerfil :LiveData<Bitmap> = _fotoPerfil
val fotosacadaAhora = mutableStateOf<Bitmap?>(null)

    fun fotoSacadaCamara(imagenNueva: Bitmap){

            _fotoPerfil.value = imagenNueva
        fotosacadaAhora.value = imagenNueva
    }





fun guardarCambios(nombre:String,email:String,nacionalidad:String){
    _nombre.postValue(nombre)
    _email.value = email
 _nacionalidad.value = nacionalidad
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