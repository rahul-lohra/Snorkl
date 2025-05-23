package rahul.lohra.networkmonitor.presentation.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import rahul.lohra.networkmonitor.Util
import rahul.lohra.networkmonitor.data.NetworkData
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel

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