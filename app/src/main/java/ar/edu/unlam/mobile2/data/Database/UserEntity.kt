package ar.edu.unlam.mobile2.data.Database

import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile2.model.UserModel


@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val nacionalidad: String,
    val imagen: ByteArray
)

fun UserModel.toDatabase() = UserEntity(
    nombre = nombre,
    email = email,
    nacionalidad = nacionalidad,
    imagen = imagen
)

fun UserEntity.toUser() = UserModel(
    nombre = nombre,
    email = email,
    nacionalidad = nacionalidad,
    imagen = imagen
)
