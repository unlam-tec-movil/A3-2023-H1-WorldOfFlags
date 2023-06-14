package ar.edu.unlam.mobile2.model

data class UserModel(
    val nombre: String,
    val email: String,
    val nacionalidad: String,
    val imagen: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (!imagen.contentEquals(other.imagen)) return false

        return true
    }

    override fun hashCode(): Int {
        return imagen.contentHashCode()
    }
}
