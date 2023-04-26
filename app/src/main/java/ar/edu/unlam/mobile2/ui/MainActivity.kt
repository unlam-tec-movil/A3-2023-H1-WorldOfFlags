package ar.edu.unlam.mobile2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import ar.edu.unlam.mobile2.R
import ar.edu.unlam.mobile2.databinding.ActivityMainBinding
import ar.edu.unlam.mobile2.model.CountryModel
import coil.compose.SubcomposeAsyncImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : /*AppCompatActivity()*/ ComponentActivity() {

    private val mainViewModel: CountriesViewModel by viewModels()
    /*
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@MainActivity,PantallaPrincipal::class.java))
        finish()
    }
}

    /*
        Log.i("MainActivity", "onCreate")

        lanzarPaises()
    }

    private fun lanzarPaises() {
        lifecycleScope.launch {
            mainViewModel.startGame()
            CoroutineScope(Dispatchers.Main).launch {
                setContent{
                    PantallaJuego(countries = mainViewModel)
                }
            }
        }
    }
/*
    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart")
    }

    @Composable
    fun content(name: String) {
        Log.i("MainActivity", "start content")
        Column() {
            Log.i("MainActivity", "button row")
            Row() {
                Button(
                    onClick = {
                        Log.i("Button", "Presiono el boton")
                        mainViewModel.updateKittyUrl()
                    },
                ) {
                    Text(text = "Actualizar imagen")
                }
            }
            Log.i("MainActivity", "first row")
            Row() {
                Text(text = "Imagen de los gatitos")
            }
            Log.i("MainActivity", "second row")
            Row() {
                SubcomposeAsyncImage(
                    model = mainViewModel.getImageRequest(LocalContext.current),
                    // placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = stringResource(R.string.cat_image),
                    contentScale = ContentScale.FillBounds,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(50.dp)
                                .width(50.dp),
                        )
                    },
                    modifier = Modifier
                        .height(300.dp)
                        .width(500.dp),
                    // error = rememberAsyncImagePainter(model = mainViewModel.DEFAULT),
                    onError = { error ->
                        run {
                            mainViewModel.updateKittyUrl()
                            Log.e("AsyncImageError", error.result.throwable.message.toString())
                        }
                    },
              /*      onLoading = { state ->
                        if (BuildConfig.DEBUG) {
                            Log.i(
                                "AsyncImageLoading",
                                state.toString(),
                            )
                        }
                    }, */

                    )
            }
            Log.i("MainActivity", "third row")
            Row() {
                Text(text = "Pie de imagen")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/


 */

}
*/