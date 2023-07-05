package ar.edu.unlam.mobile2.RepoViejo

interface KittiesRepo {

    suspend fun getNewKitty(): String
}
