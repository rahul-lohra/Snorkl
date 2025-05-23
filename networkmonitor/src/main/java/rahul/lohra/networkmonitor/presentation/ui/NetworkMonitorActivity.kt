package rahul.lohra.networkmonitor.presentation.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.Util
import rahul.lohra.networkmonitor.data.NetworkData
import rahul.lohra.networkmonitor.data.RestApiData
import rahul.lohra.networkmonitor.data.WebsocketData
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.repository.NetworkRepositoryImpl
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.presentation.data.UiFail
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiLoading
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodel
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodelFactory

class NetworkMonitorActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(applicationContext)

        val networkRepository = NetworkRepositoryImpl(db.networkLogDao())
        val useCase = GetPagedNetworkLogsUseCase(networkRepository)

        setContent {
            MyMonitorTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    NetworkMonitorToolbar(Modifier.fillMaxWidth(), {

                    },{

                    },{

                    })
                }) { innerPadding ->
                    Root(modifier = Modifier.padding(innerPadding), useCase)
                }
            }
        }
    }
}

val LocalNetworkMonitorViewModel = staticCompositionLocalOf<NetworkMonitorViewmodel> {
    error("NetworkMonitorViewmodel not provided")
}

@Composable
fun Root(modifier: Modifier, useCase: GetPagedNetworkLogsUseCase){

    val viewModel: NetworkMonitorViewmodel = viewModel(
        key = NetworkMonitorViewmodel.KEY,
        factory = NetworkMonitorViewmodelFactory(useCase)
    )
    CompositionLocalProvider(LocalNetworkMonitorViewModel provides viewModel) {
        MyAppNavHost(modifier) // No need to pass the VM here
    }
}

@Composable
fun MyAppNavHost(modifier: Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(modifier, navController) }
        composable("details") { DetailsScreen(modifier, navController) }
    }
}

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NetworkMonitorUi(modifier, navController)
        Button(onClick = { navController.navigate("details") }) {
            Text("Go to Details")
        }
        val viewModel = LocalNetworkMonitorViewModel.current
        Button(onClick = {
            viewModel.logs
        }) {
            Text("GET Request")
        }
    }
}
//
@Composable
fun DetailsScreen(modifier: Modifier, navController: NavController) {
    val viewModel = LocalNetworkMonitorViewModel.current
    val data: UiState<NetworkData> by viewModel.detailScreenData.collectAsStateWithLifecycle(
        UiInitial()
    )
    val context = LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        DetailScreenToolbar(Modifier.fillMaxWidth(), {
        },{
            if(data is UiSuccess){
                val text = (data as UiSuccess<NetworkData>).data.toString()
                Util.shareText(context, text)
            }
        })
    }) { innerPadding ->
        DetailsScreenBody(modifier = Modifier.padding(innerPadding))

    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("This is the Details Screen")
    }
}

@Composable
fun DetailsScreenBody(modifier: Modifier){
    val viewModel = LocalNetworkMonitorViewModel.current
    val data: UiState<NetworkData> by viewModel.detailScreenData.collectAsStateWithLifecycle(
        UiInitial()
    )
    when(data){
        is UiSuccess -> {
            Text((data as UiSuccess<NetworkData>).data.toString())
        }
        else -> {
            Text("No data")
        }
    }
}



@Composable
fun NetworkMonitorUi(modifier: Modifier, navController: NavController) {
//    val viewModel: NetworkMonitorViewmodel = viewModel(key = NetworkMonitorViewmodel.KEY, factory = )
    val viewModel = LocalNetworkMonitorViewModel.current
    val state2: UiState<List<NetworkListItem>> by viewModel.networkUiList.collectAsStateWithLifecycle()
    when (state2) {
        is UiInitial -> {
            Text("Initial")
        }
        is UiSuccess -> {
            val networkRecords = (state2 as UiSuccess<List<NetworkListItem>>).data
            val onItemClick = { networkData:NetworkData ->
                viewModel.setDetailScreenData(networkData)
                navController.navigate("details")
            }
            LazyColumn{
                items(networkRecords.size, key = { index -> networkRecords[index].hashCode() }) { index ->
                    val item = networkRecords[index].data
                    if( item is RestApiData) {
                        RestApiListItem(modifier, item, onItemClick)
                    }else  {
                        WebsocketListItem(modifier, item as WebsocketData, onItemClick)
                    }
                }
            }
        }
        is UiLoading -> {
            Text("Loading...")
        }
        is UiFail -> {
            Text("Failed")
        }
    }

}

@Composable
fun RestApiListItem(modifier: Modifier, restApiData: RestApiData, onItemClick: (NetworkData)->Unit) {
    Row(modifier = modifier.clickable {
        onItemClick(restApiData)
    }) {
        Text("${restApiData.responseCode}")
        Spacer(Modifier.width(12.dp))
        Column {
            Text(restApiData.method.capitalize())
            Spacer(Modifier.width(4.dp))
            Text(Uri.parse(restApiData.requestUrl).path ?: restApiData.requestUrl)
            Text(Uri.parse(restApiData.requestUrl).host ?: "")
            Row {
                Text("${restApiData.timestamp}")
                Text("${restApiData.durationMs}")
            }
        }
    }
}

@Composable
fun WebsocketListItem(modifier: Modifier, websocketData: WebsocketData, onItemClick: (NetworkData)->Unit) {
    Row(modifier = modifier.clickable {
        onItemClick(websocketData)
    }) {
        Text("Response Code")
        Column {
            Text("Url")
            Text("Host")
            Row {
                Text("${websocketData.timestamp}")
//                Text("${websocketData.durationMs}")
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreenToolbar(
    modifier: Modifier,
    onSearchClick: () -> Unit,
    onShareClick: () -> Unit,
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}


