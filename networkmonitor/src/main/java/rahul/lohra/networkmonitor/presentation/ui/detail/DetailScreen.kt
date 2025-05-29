package rahul.lohra.networkmonitor.presentation.ui.detail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rahul.lohra.networkmonitor.Util
import rahul.lohra.networkmonitor.core.TimeUtil
import rahul.lohra.networkmonitor.data.NetworkData
import rahul.lohra.networkmonitor.data.local.db.DatabaseProvider
import rahul.lohra.networkmonitor.data.local.entities.NetworkType
import rahul.lohra.networkmonitor.data.repository.NetworkRepositoryImpl
import rahul.lohra.networkmonitor.domain.usecas.GetPagedNetworkLogsUseCase
import rahul.lohra.networkmonitor.domain.usecas.RequestResponseRetrieverUseCase
import rahul.lohra.networkmonitor.domain.usecas.ShareUseCase
import rahul.lohra.networkmonitor.presentation.data.UiInitial
import rahul.lohra.networkmonitor.presentation.data.UiState
import rahul.lohra.networkmonitor.presentation.data.UiSuccess
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel
import rahul.lohra.networkmonitor.presentation.ui.detail.viewmodel.DetailViewmodel
import rahul.lohra.networkmonitor.presentation.ui.detail.viewmodel.DetailViewmodelFactory
import rahul.lohra.networkmonitor.presentation.viewmodel.NetworkMonitorViewmodel

@Composable
fun DetailsScreen(modifier: Modifier, navController: NavController, id: String) {

    val context = LocalContext.current.applicationContext

    val db = remember { DatabaseProvider.getDatabase(context) }
    val networkRepository = remember { NetworkRepositoryImpl(db.networkLogDao()) }
    val useCase = remember { RequestResponseRetrieverUseCase(networkRepository) }
    val shareUseCase = remember { ShareUseCase(networkRepository) }

    val viewModel: DetailViewmodel = viewModel(
        key = DetailViewmodel.KEY,
        factory = DetailViewmodelFactory(useCase, shareUseCase)
    )

    CompositionLocalProvider(LocalDetailViewModel provides viewModel) {
        val data: UiState<DetailScreenUiModel> by viewModel.detailScreenData.collectAsStateWithLifecycle(
            UiInitial()
        )
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            DetailScreenToolbar(Modifier.fillMaxWidth(), {
            }, {
                if (data is UiSuccess) {
                    val text = (data as UiSuccess<DetailScreenUiModel>).data.toString()
                    Util.shareText(context, text)
                }
            })
        }) { innerPadding ->
            DetailsScreenBody(modifier = Modifier.padding(innerPadding), id)
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

    when (data) {
        is UiSuccess -> {
            Text((data as UiSuccess<DetailScreenUiModel>).data.toString())
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

val LocalDetailViewModel = staticCompositionLocalOf<DetailViewmodel> {
    error("DetailViewmodel not provided")
}

@Composable
fun RestRequestDetails(restApiDetails: RestApiDetails) {
    Column {
        Row {
            TitleText("Url:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.requestUrl)
        }
        Row {
            TitleText("Method:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.method)
        }
        Row {
            TitleText("Status code:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.responseCode.toString())
        }
        Row {
            TitleText("Timestamp:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(TimeUtil.formatTimestamp(restApiDetails.timestamp))
        }
        Row {
            TitleText("Headers:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.requestHeaders.toString())
        }
        Row {
            TitleText("Response Size:")
            Spacer(Modifier.width(4.dp))
            DescriptionText("${restApiDetails.body.length / 1024} kb")
        }
        Row {
            TitleText("Response:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.body)
        }
    }
}

@Preview
@Composable
fun PreviewRestRequestDetails() {
    val restApiDetails = RestApiDetails(
        id = "8a6afe0e-9d53-4a8d-a5c3-f5b06d47e186",
        networkType = NetworkType.REST.title,
        timestamp = 1748493681789,
        requestUrl = "https://echo.websocket.events/",
        method = "GET",
        requestHeaders = emptyMap(),
        responseCode = 200,
        responseHeaders = parseJsonWithGson(
            """
            {"access-control-allow-origin":["*"],"content-type":["application/json; charset=UTF-8"],"date":["Thu, 29 May 2025 11:53:05 GMT"],"fastcgi-cache":["BYPASS"],"referrer-policy":["strict-origin-when-cross-origin"],"response-status-code":["200"],"server":["nginx"],"strict-transport-security":["max-age=31536000; includeSubDomains"],"vary":["Accept-Encoding"],"x-content-type-options":["nosniff"],"x-frame-options":["SAMEORIGIN"],"x-xss-protection":["1; mode=block"]}
        """.trimIndent()
        ),
        body = """
            ["Kickstarter +1 hexagon chia leggings, vape authentic flexitarian mukbang live-edge.  Woke succulents cray, tumblr selvage viral prism food truck kogi gochujang drinking vinegar chillwave authentic ramps vape.  Health goth activated charcoal bruh fam.  Semiotics irony prism pickled YOLO fashion axe meh yes plz.  Tacos vegan portland, subway tile sustainable cred pabst godard etsy jawn gentrify.  Shabby chic austin ramps mixtape hot chicken lyft copper mug chartreuse subway tile kitsch waistcoat solarpunk snackwave portland pug.  Live-edge selfies solarpunk, Brooklyn blue bottle church-key pug.","Glossier bicycle rights tacos, big mood post-ironic man bun marfa.  Banh mi asymmetrical messenger bag organic readymade fixie salvia cornhole mukbang try-hard tonx.  Farm-to-table deep v mukbang fingerstache occupy same jawn hot chicken.  Tofu kinfolk yuccie tonx shoreditch selfies freegan scenester put a bird on it polaroid.  Gastropub cloud bread 90's godard deep v portland vape everyday carry kombucha actually.  Pinterest you probably haven't heard of them kale chips, authentic franzen master cleanse trust fund tilde hammock XOXO coloring book biodiesel."]
        """.trimIndent(),
        durationMs = 8348,
        requestBody = ""
    )
    RestRequestDetails(restApiDetails)
}

fun parseJsonWithGson(jsonString: String): HashMap<String, List<String>> {
    val type = object : TypeToken<HashMap<String, List<String>>>() {}.type
    return Gson().fromJson(jsonString, type)
}

@Composable
fun DescriptionText(text: String) {
    val isInDarkMode = isSystemInDarkTheme()
    val color = if (isInDarkMode) {
        Color(0xffD1D5DB)
    } else {
        Color(0xff374151)
    }

    Text(text, color = color)
}

@Composable
fun TitleText(text: String) {
    val isInDarkMode = isSystemInDarkTheme()
    val color = if (isInDarkMode) {
        Color(0xff6B7280)
    } else {
        Color(0xff9CA3AF)
    }
    Text(text, color = color)
}