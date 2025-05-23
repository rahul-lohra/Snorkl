package rahul.lohra.networkmonitor.presentation.ui.home

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.RestApiListItem
import rahul.lohra.networkmonitor.WebsocketListItem
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
fun RestApiListItemUi(
    modifier: Modifier,
    restApiData: RestApiListItem,
    onItemClick: (NetworkListItem) -> Unit
) {
    Row(modifier = Modifier.clickable {
        onItemClick(restApiData)
    }.fillMaxWidth()) {
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
fun WebsocketListItemUi(
    modifier: Modifier,
    websocketData: WebsocketListItem,
    onItemClick: (NetworkListItem) -> Unit
) {
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

@Composable
fun NetworkMonitorUi(
    modifier: Modifier,
    networkLogTab: NetworkLogTab,
    navController: NavController
) {
    val viewModel = LocalNetworkMonitorViewModel.current
//    val state2: UiState<List<NetworkListItem>> by viewModel.networkUiList.collectAsStateWithLifecycle()
    val state = viewModel.allUiNetworkLogs.collectAsLazyPagingItems()
    NetworkLogScreenContent(
        modifier,
        networkLogTab,
        navController, state
    )
//    when (state2) {
//        is UiInitial -> {
//            Text("Initial")
//        }
//        is UiSuccess -> {
//            val networkRecords = (state2 as UiSuccess<List<NetworkListItem>>).data
//            val onItemClick = { networkData: NetworkListItem ->
//                viewModel.setDetailScreenData(networkData)
//                navController.navigate("details")
//            }
//            LazyColumn{
//                items(networkRecords.size, key = { index -> networkRecords[index].hashCode() }) { index ->
//                    val item = networkRecords[index]
//                    if( item is RestApiListItem) {
//                        RestApiListItemUi(modifier, item, onItemClick)
//                    }else  {
//                        WebsocketListItemUi(modifier, item as WebsocketListItem, onItemClick)
//                    }
//                }
//            }
//        }
//        is UiLoading -> {
//            Text("Loading...")
//        }
//        is UiFail -> {
//            Text("Failed")
//        }
//    }
}

@Composable
fun NetworkLogScreenContent(
    modifier: Modifier,
    networkLogTab: NetworkLogTab,
    navController: NavController,
    lazyItems: LazyPagingItems<NetworkListItem>
) {
    when {
        lazyItems.loadState.refresh is LoadState.Loading -> {
            LoadingView()
        }

        lazyItems.loadState.refresh is LoadState.Error -> {
            val e = lazyItems.loadState.refresh as LoadState.Error
            ErrorView(message = e.error.localizedMessage ?: "Unknown error")
        }

        lazyItems.itemCount == 0 && lazyItems.loadState.refresh is LoadState.NotLoading -> {
            EmptyView(message = "No logs to display")
        }

        else -> {
            val viewModel = LocalNetworkMonitorViewModel.current
            val onItemClick = { networkData: NetworkListItem ->
                viewModel.setDetailScreenData(networkData)
                navController.navigate("details")
            }
            LazyColumn {
                items(lazyItems.itemCount) { index ->
                    lazyItems[index]?.let { item ->
                        if (item is RestApiListItem) {
                            RestApiListItemUi(modifier, item, onItemClick)
                        } else {
                            WebsocketListItemUi(modifier, item as WebsocketListItem, onItemClick)
                        }
                    }
                }

                // Optional: Show loading at the end for pagination
                if (lazyItems.loadState.append is LoadState.Loading) {
                    item {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }

                if (lazyItems.loadState.append is LoadState.Error) {
                    item {
                        ErrorView(message = "Failed to load more logs")
                    }
                }
            }
        }
    }
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