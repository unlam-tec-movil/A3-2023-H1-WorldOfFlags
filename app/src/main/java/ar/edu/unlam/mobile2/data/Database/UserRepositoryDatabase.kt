package ar.edu.unlam.mobile2.data.Database

import ar.edu.unlam.mobile2.model.UserModel
import javax.inject.Inject


class UserRepositoryDatabase @Inject constructor(private val database: UserDatabase) {


    suspend fun traerUserDatabase(): UserModel {
        val response: List<UserEntity> = database.userDao().getAllUser()
        return response[0].toUser()
    }


    suspend fun agregarUsuario(user: UserEntity) {
        database.userDao().insert(user)
    }

}