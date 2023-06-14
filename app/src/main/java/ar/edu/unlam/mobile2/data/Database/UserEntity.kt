package ar.edu.unlam.mobile2.data.Database

import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile2.model.UserModel


@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombre_usuario")
    val nombre: String,
    @ColumnInfo(name = "email_usuario")
    val email: String,
    @ColumnInfo(name = "nacionalidad_usuario")
    val nacionalidad: String,
    @ColumnInfo(name = "imagen_usuario")
    val imagen: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (!imagen.contentEquals(other.imagen)) return false

        return true
    }

    override fun hashCode(): Int {
        return imagen.contentHashCode()
    }
}

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
