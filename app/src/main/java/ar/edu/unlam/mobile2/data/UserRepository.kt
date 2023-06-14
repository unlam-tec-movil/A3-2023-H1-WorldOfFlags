package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.UserModel

object UserRepository {
    private var user: UserModel? = null

    fun getUser(): UserModel? {
        return user
    }

    fun setUser(newUser: UserModel) {
        user = newUser
    }

}