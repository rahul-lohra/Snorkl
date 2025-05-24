package rahul.lohra.networkmonitor.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import rahul.lohra.networkmonitor.presentation.ui.detail.DetailsScreen
import rahul.lohra.networkmonitor.presentation.ui.home.HomeScreen
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodel
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodelFactory

class NetworkMonitorActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(applicationContext)

        val networkRepository = NetworkRepositoryImpl(db.networkLogDao())
        val useCase = GetPagedNetworkLogsUseCase(networkRepository)

        setContent {
            val viewModel: NetworkMonitorViewmodel = viewModel(
                key = NetworkMonitorViewmodel.KEY,
                factory = NetworkMonitorViewmodelFactory(useCase)
            )
            CompositionLocalProvider(LocalNetworkMonitorViewModel provides viewModel) {
                MyMonitorTheme {
                    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                        NetworkMonitorToolbar(Modifier.fillMaxWidth(), {
                            viewModel.makeGetRequest()
                        },{

                        },{

                        })
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
fun Root(modifier: Modifier){
    MyAppNavHost(modifier)
//    val viewModel: NetworkMonitorViewmodel = viewModel(
//        key = NetworkMonitorViewmodel.KEY,
//        factory = NetworkMonitorViewmodelFactory(useCase)
//    )
//    CompositionLocalProvider(LocalNetworkMonitorViewModel provides viewModel) {
//        // No need to pass the VM here
//    }
}

@Composable
fun MyAppNavHost(modifier: Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(modifier, navController) }
        composable("details") { DetailsScreen(modifier, navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkMonitorToolbar(
    modifier: Modifier,
    onSearchClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text("My Toolbar") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = onShareClick) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}


