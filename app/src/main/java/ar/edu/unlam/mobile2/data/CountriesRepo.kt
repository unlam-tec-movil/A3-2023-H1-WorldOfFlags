package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.CountryModel

interface CountriesRepo {

    suspend fun getCountry(): List<CountryModel>?
    
    suspend fun getCountryByName(name: String): List<CountryModel>?
}