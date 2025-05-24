package rahul.lohra.networkmonitor.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HomeScreenTabs(modifier, navController)
//        ApiCallsScreen()
//
    }
}

@Composable
fun ApiCallsScreen() {
    val viewModel = LocalNetworkMonitorViewModel.current
    Button(onClick = {
        viewModel.makeGetRequest()
    }) {
        Text("GET Request")
    }
}

@Composable
fun HomeScreenTabs(modifier: Modifier, navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(NetworkLogTab.ALL, NetworkLogTab.HTTP, NetworkLogTab.WEBSOCKET)
    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, networkTab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(networkTab.title) }
                )
            }
        }
        NetworkMonitorUi(modifier, tabs[selectedTabIndex], navController)
    }
}



@Composable
fun NetworkMonitorUi(
    modifier: Modifier,
    networkLogTab: NetworkLogTab,
    navController: NavController
) {
    val viewModel = LocalNetworkMonitorViewModel.current
    val state = viewModel.allUiNetworkLogs.collectAsLazyPagingItems()
    NetworkLogScreenContent(
        modifier,
        networkLogTab,
        navController, state
    )
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message)
    }
}

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* retry logic */ }) {
                Text("Retry")
            }
        }
    }
}

enum class NetworkLogTab(val title: String) {
    ALL("All"),
    HTTP("HTTP"),
    WEBSOCKET("WebSocket")
}