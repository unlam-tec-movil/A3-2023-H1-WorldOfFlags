package ar.edu.unlam.mobile2.model

import androidx.compose.ui.graphics.ImageBitmap

data class UsuarioModel(
    val nombre: String,
    val email: String,
    val nacionalidad: String,
    val imagen: ImageBitmap
)
