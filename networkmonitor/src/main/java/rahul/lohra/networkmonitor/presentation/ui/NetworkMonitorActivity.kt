package rahul.lohra.networkmonitor.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.repository.NetworkRepositoryImpl
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailsScreen
import rahul.lohra.networkmonitor.presentation.ui.home.HomeScreen
import rahul.lohra.networkmonitor.presentation.ui.home.NetworkMonitorToolbarRoot
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodel
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodelFactory

class NetworkMonitorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(applicationContext)

        val networkRepository = NetworkRepositoryImpl(db.networkLogDao())
        val useCase = GetPagedNetworkLogsUseCase(networkRepository)
        val shareUseCase = ShareUseCase(networkRepository)

        setContent {
            val viewModel: NetworkMonitorViewmodel = viewModel(
                key = NetworkMonitorViewmodel.KEY,
                factory = NetworkMonitorViewmodelFactory(useCase, shareUseCase)
            )
            CompositionLocalProvider(LocalNetworkMonitorViewModel provides viewModel) {
                MyMonitorTheme {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        NetworkMonitorToolbarRoot()
                    }) { innerPadding ->
                        Root(modifier = Modifier.padding(innerPadding))
                    }
                } // No need to pass the VM here
            }

        }
    }
}

val LocalNetworkMonitorViewModel = staticCompositionLocalOf<NetworkMonitorViewmodel> {
    error("NetworkMonitorViewmodel not provided")
}

@Composable
fun Root(modifier: Modifier) {
    MyAppNavHost(modifier)
}

@Composable
fun MyAppNavHost(modifier: Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(modifier, navController) }
        composable("details") { DetailsScreen(modifier, navController) }
    }
}

