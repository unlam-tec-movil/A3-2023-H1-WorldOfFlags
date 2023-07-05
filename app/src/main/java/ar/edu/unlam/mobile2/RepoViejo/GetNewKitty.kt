package ar.edu.unlam.mobile2.RepoViejo

import ar.edu.unlam.mobile2.RepoViejo.KittiesRepo
import javax.inject.Inject

class GetNewKitty {

    var repo: KittiesRepo

    @Inject
    constructor(repo: KittiesRepo) {
        this.repo = repo
    }

    suspend fun getKitty(): String {
        return repo.getNewKitty()
    }
}
