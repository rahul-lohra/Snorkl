package rahul.lohra.networkmonitor.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel
import rahul.lohra.networkmonitor.presentation.ui.MyMonitorTheme
import rahul.lohra.networkmonitor.presentation.ui.Root

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    MyMonitorTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            NetworkMonitorToolbarRoot()
        }) { innerPadding ->
            Column(
                modifier = modifier.fillMaxSize().padding(innerPadding),
            ) {
                HomeScreenTabs(modifier, navController)
            }
        }
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

    val tabs = listOf(NetworkLogTab.ALL, NetworkLogTab.HTTP, NetworkLogTab.WEBSOCKET)
    val pagerState = rememberPagerState{
        tabs.size
    }
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, networkTab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(networkTab.title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState
        ) { page ->
            when (tabs[page]) {
                NetworkLogTab.ALL -> NetworkMonitorUi(modifier, tabs[page], navController)
                NetworkLogTab.HTTP -> NetworkMonitorUi(modifier, tabs[page], navController)
                NetworkLogTab.WEBSOCKET -> NetworkMonitorUi(modifier, tabs[page], navController)
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
    val lazyItems = when (networkLogTab) {
        NetworkLogTab.ALL -> viewModel.allUiNetworkLogs.collectAsLazyPagingItems()
        NetworkLogTab.HTTP -> viewModel.httpNetworkLogs.collectAsLazyPagingItems()
        NetworkLogTab.WEBSOCKET -> viewModel.wsNetworkLogs.collectAsLazyPagingItems()
    }

    NetworkLogScreenContent(
        modifier,
        networkLogTab,
        navController,
        lazyItems
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