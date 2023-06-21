package ar.edu.unlam.mobile2.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.data.Database.UserEntity
import ar.edu.unlam.mobile2.data.UserRepositoryDatabase
import ar.edu.unlam.mobile2.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.nio.ByteBuffer
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepositoryDatabase) :
    ViewModel() {


    val userName = MutableLiveData<String>()
    val nacionalityUser = MutableLiveData<String>()
    val imageUser = MutableLiveData<ByteArray>()
    val emailUser = MutableLiveData<String>()


    fun getUserDatabase() {
        val user = userRepository.traerUserDatabase()
        userName.value = user.nombre
        nacionalityUser.value = user.nacionalidad
        imageUser.value = user.imagen
        emailUser.value = user.email

    }

    fun setUserDatabase(user: UserEntity) {
        userRepository.agregarUsuario(user)
    }

    fun cantidadUsuarios(): Int {
        return userRepository.devolverUsuarios()
    }

    fun getAllUserDatabase(): List<UserModel> {
        return userRepository.getAllUser()
    }

}