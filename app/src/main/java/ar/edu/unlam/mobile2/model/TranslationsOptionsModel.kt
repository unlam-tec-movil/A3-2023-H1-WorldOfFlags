package ar.edu.unlam.mobile2.model

import java.io.Serializable

data class TranslationsOptionsModel(
    val official: String,
    val common: String
) : Serializable
