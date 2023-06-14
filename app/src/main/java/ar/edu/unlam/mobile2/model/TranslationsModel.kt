package ar.edu.unlam.mobile2.model

import java.io.Serializable

data class TranslationsModel(
    val por : TranslationsOptionsModel,
    val spa : TranslationsOptionsModel
    ) : Serializable
