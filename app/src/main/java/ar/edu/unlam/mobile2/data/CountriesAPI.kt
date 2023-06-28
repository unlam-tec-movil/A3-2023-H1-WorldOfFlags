package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.CountryModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesAPI {

    @GET("v3.1/all")
    suspend fun getCountries(): Response<List<CountryModel>>
    
    @GET("v3.1/name/{name}")
    suspend fun getCountryByName(@Path("name") name:String): Response<List<CountryModel>>
}