package ar.edu.unlam.mobile2.ui.ViewModel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile2.data.Database.UserDao
import ar.edu.unlam.mobile2.data.Database.UserEntity
import ar.edu.unlam.mobile2.data.Database.UserRepositoryDatabase
import ar.edu.unlam.mobile2.data.UserRepository
import ar.edu.unlam.mobile2.domain.CountriesService
import ar.edu.unlam.mobile2.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepositoryDatabase) :
    ViewModel() {


    val userName= MutableLiveData<String>()
    val nacionalityUser= MutableLiveData<String>()
    val imageUser= MutableLiveData<ByteArray>()



   suspend fun getUserDatabase() {
       val user= userRepository.traerUserDatabase()
       userName.value=user.nombre
       nacionalityUser.value=user.nacionalidad
       imageUser.value=user.imagen
   }

    suspend fun setUserDatabase(user:UserEntity){
        userRepository.agregarUsuario(user)
    }

}