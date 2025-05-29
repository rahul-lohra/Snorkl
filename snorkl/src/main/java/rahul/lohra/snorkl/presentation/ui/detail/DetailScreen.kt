package rahul.lohra.snorkl.presentation.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import rahul.lohra.snorkl.data.local.db.DatabaseProvider
import rahul.lohra.snorkl.data.local.entities.NetworkType
import rahul.lohra.snorkl.data.repository.NetworkRepositoryImpl
import rahul.lohra.snorkl.domain.usecas.ClearDataUseCase
import rahul.lohra.snorkl.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.snorkl.domain.usecas.ShareUseCase
import rahul.lohra.snorkl.presentation.data.UiInitial
import rahul.lohra.snorkl.presentation.data.UiSuccess
import rahul.lohra.snorkl.presentation.ui.MyMonitorTheme
import rahul.lohra.snorkl.presentation.ui.ShareIntentObserver
import rahul.lohra.snorkl.presentation.ui.ShareMenu
import rahul.lohra.snorkl.presentation.ui.detail.viewmodel.DetailViewmodel
import rahul.lohra.snorkl.presentation.ui.detail.viewmodel.DetailViewmodelFactory

@Composable
fun DetailsScreen(modifier: Modifier, navController: NavController, id: String) {

    val context = LocalContext.current.applicationContext

    val db = remember { DatabaseProvider.getDatabase(context) }
    val networkRepository = remember { NetworkRepositoryImpl(db.networkLogDao()) }
    val useCase = remember { RequestResponseRetrieverUseCase(networkRepository) }
    val shareUseCase = remember { ShareUseCase(networkRepository) }
    val clearDataUseCase = remember { ClearDataUseCase(networkRepository) }

    val viewModel: DetailViewmodel = viewModel(
        key = DetailViewmodel.KEY,
        factory = DetailViewmodelFactory(useCase, clearDataUseCase, shareUseCase, id)
    )

    CompositionLocalProvider(LocalDetailViewModel provides viewModel) {

        MyMonitorTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { DetailScreenToolbarRoot(navController, id) }) { innerPadding ->
                DetailsScreenBody(modifier = Modifier.padding(innerPadding), id)
            }
        }
    }
}

@Composable
fun DetailsScreenBody(modifier: Modifier, id: String) {
    val viewModel = LocalDetailViewModel.current
    val data by viewModel.detailScreenData.collectAsStateWithLifecycle(UiInitial())
    LaunchedEffect(Unit) {
        viewModel.onDetailBodyUiEvent(DetailBodyUiEvent.OnGetDetail(id))
    }
    Box(modifier = modifier) {
        when (data) {
            is UiSuccess -> {
                val detailScreenUiModel = (data as UiSuccess<DetailScreenUiModel>).data
                if (detailScreenUiModel.networkType == NetworkType.REST.title) {
                    RestApiDetailsUi(detailScreenUiModel as RestApiDetails)
                } else {
                    WsDetailsUi(detailScreenUiModel as WsDetails)
                }
            }

            else -> {
                Text("No data")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScreenToolbar(
    modifier: Modifier,
    onSearchClick: () -> Unit,
    onShareJsonClick: () -> Unit,
    onShareTextClick: () -> Unit,
    onExportFromDeviceClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text("Details Screen") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            ShareMenu(onShareJsonClick, onShareTextClick, onExportFromDeviceClick)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Composable
fun DetailScreenToolbarRoot(navController: NavController, id: String) {
    val viewModel = LocalDetailViewModel.current

    ShareIntentObserver(viewModel)
    DetailScreenToolbar(
        Modifier.fillMaxWidth(),
        onSearchClick = { viewModel.onToolbarUiEvent(ToolbarUiEvent.OnSearchClick(id)) },
        onShareJsonClick = { viewModel.onToolbarUiEvent(ToolbarUiEvent.OnShareJsonClick(id)) },
        onShareTextClick = { viewModel.onToolbarUiEvent(ToolbarUiEvent.OnShareTextClick(id)) },
        onExportFromDeviceClick = { viewModel.onToolbarUiEvent(ToolbarUiEvent.OnExportFromDeviceClick(id)) },
        onBackClick = { navController.popBackStack() },
    )
}

val LocalDetailViewModel = staticCompositionLocalOf<DetailViewmodel> {
    error("DetailViewmodel not provided")
}

