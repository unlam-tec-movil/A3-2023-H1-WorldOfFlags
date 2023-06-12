package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.UsuarioModel

object UserRepository {
    private var user: UsuarioModel? = null

    fun getUser(): UsuarioModel? {
        return user
    }

    fun setUser(newUser: UsuarioModel) {
        user = newUser
    }

}