package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.CountryModel
import retrofit2.Response
import retrofit2.http.GET

interface CountriesAPI {

    @GET("v3.1/all")
    suspend fun getCountries(): Response<List<CountryModel>>
}