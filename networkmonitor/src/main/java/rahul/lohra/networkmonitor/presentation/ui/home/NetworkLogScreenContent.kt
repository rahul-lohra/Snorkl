package rahul.lohra.networkmonitor.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.RestApiListItem
import rahul.lohra.networkmonitor.WebsocketListItem
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel

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
                        Box(Modifier.background(Color.White)) { //Day Color White, night colot 0xff1f2937
                            if (item is RestApiListItem) {
                                RestApiListItemUi(modifier, item, onItemClick)
                            } else {
                                WebsocketListItemUi(
                                    modifier,
                                    item as WebsocketListItem,
                                    onItemClick
                                )
                            }
                        }
                        if (index < lazyItems.itemCount) {
                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = Color(0xFFE5E7EB),
                            )
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