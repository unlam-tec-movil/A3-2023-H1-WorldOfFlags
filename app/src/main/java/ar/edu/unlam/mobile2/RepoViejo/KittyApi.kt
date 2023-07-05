package ar.edu.unlam.mobile2.RepoViejo

import ar.edu.unlam.mobile2.RepoViejo.KittyApiModel
import retrofit2.Response
import retrofit2.http.GET

interface KittyApi {

    @GET("/v1/images/search?limit=3")
    suspend fun getKitties(): Response<List<KittyApiModel>>
}
