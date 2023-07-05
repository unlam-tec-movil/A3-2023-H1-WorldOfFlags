package ar.edu.unlam.mobile2.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import ar.edu.unlam.mobile2.ui.ViewModel.UserViewModel
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :  ComponentActivity() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, "3f8e54d9-dcaf-4b4e-bfd6-bb0527ccf0c1",
            Analytics::class.java, Crashes::class.java
        )

if(viewModel.cantidadUsuarios()==0) {
    startActivity(Intent(this@MainActivity, PantallaInicioSinUsuario::class.java))
    finish()
}else{
    startActivity(Intent(this@MainActivity, PantallaPrincipal::class.java))
    finish()
}
    }
}

