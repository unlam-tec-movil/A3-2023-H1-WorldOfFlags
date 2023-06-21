package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.data.Database.UserDao
import ar.edu.unlam.mobile2.data.Database.UserEntity
import ar.edu.unlam.mobile2.data.Database.toUser
import ar.edu.unlam.mobile2.model.UserModel
import javax.inject.Inject


class UserRepositoryDatabase @Inject constructor(private val dao: UserDao) {


    fun agregarUsuario(user: UserEntity) {
        dao.insert(user)
    }

    fun traerUserDatabase(): UserModel {
        val response: List<UserEntity> = dao.getAllUser()
        return response[0].toUser()
    }
    fun devolverUsuarios():Int{
        val response: List<UserEntity> = dao.getAllUser()
        return response.size
    }

    fun getAllUser():List<UserModel>{
        val response: List<UserEntity> = dao.getAllUser()
        return response.map { it.toUser() }
    }




}