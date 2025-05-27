package rahul.lohra.networkmonitor.presentation.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.mapNotNull
import rahul.lohra.networkmonitor.domain.usecas.ExportData
import rahul.lohra.networkmonitor.presentation.ui.LocalNetworkMonitorViewModel


@Composable
fun NetworkMonitorToolbarRoot() {
    val viewModel = LocalNetworkMonitorViewModel.current
    ShareIntentObserver()
    NetworkMonitorToolbarBody(Modifier.fillMaxWidth(), {
        viewModel.onToolbarUiEvent(HomeToolbarUiEvent.OnSearchClick)
        viewModel.makeGetRequest()
    }, {
        viewModel.onToolbarUiEvent(HomeToolbarUiEvent.OnDeleteClick)
    }, {
        viewModel.onToolbarUiEvent(HomeToolbarUiEvent.OnShareJsonClick)
    }, {
        viewModel.onToolbarUiEvent(HomeToolbarUiEvent.OnShareTextClick)
    }, {
        viewModel.onToolbarUiEvent(HomeToolbarUiEvent.onExportFromDeviceClick)
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkMonitorToolbarBody(
    modifier: Modifier,
    onSearchClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareJsonClick: () -> Unit,
    onShareTextClick: () -> Unit,
    onExportFromDeviceClick: () -> Unit
) {
    val context = LocalContext.current
    val appName = context.applicationInfo.loadLabel(context.packageManager).toString()

    TopAppBar(modifier = modifier, title = { Text(appName) }, actions = {
        IconButton(onClick = onSearchClick) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
        ShareMenu(onShareJsonClick, onShareTextClick, onExportFromDeviceClick)

        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    )
    )
}

@Composable
fun ShareMenu(
    onShareJsonClick: () -> Unit, onShareTextClick: () -> Unit, onExportFromDeviceClick: () -> Unit
) {
    var showShareMenu by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { showShareMenu = true }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
        }

        DropdownMenu(expanded = showShareMenu, onDismissRequest = { showShareMenu = false }) {
            DropdownMenuItem(text = { Text("Export as JSON") }, onClick = {
                showShareMenu = false
                onShareJsonClick()
            })
            DropdownMenuItem(text = { Text("Export as Text") }, onClick = {
                showShareMenu = false
                onShareTextClick()
            })

            DropdownMenuItem(text = { Text("Export from Device") }, onClick = {
                showShareMenu = false
                onExportFromDeviceClick()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtractFileInstructionsBottomSheet(
    textFilePath: String,
    jsonFilePath: String,
    onDismiss: () -> Unit,
) {
    val adbCommandForTextFile = "adb -s <DeviceSerial> pull $textFilePath ~/Downloads/"
    val adbCommandForJsonFile = "adb -s <DeviceSerial> pull $jsonFilePath ~/Downloads/"

    ModalBottomSheet(
        onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Extract Logs from Emulator",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = """
                    1️⃣ Open terminal on your Mac.
                    2️⃣ Run the following command:
                    
                    $adbCommandForTextFile
                    
                    $adbCommandForJsonFile
                    
                    3️⃣ File will be saved in your Downloads folder.
                    
                    4️⃣ If not found, check Android device's file location.
                """.trimIndent(),
                fontSize = 16.sp,
                lineHeight = 22.sp
            )


            val context = LocalContext.current
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("ADB Command", adbCommandForTextFile)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Command copied to clipboard!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Copy adb command to extract text file", fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("ADB Command", adbCommandForTextFile)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Command copied to clipboard!", Toast.LENGTH_SHORT).show()
            }) {
                Text("Copy adb command to extract json file", fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onDismiss) {
                Text("Got It", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ShareIntentObserver() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel = LocalNetworkMonitorViewModel.current

    var exportData by remember { mutableStateOf<Pair<ExportData, ExportData>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.shareIntentFlow.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { exportData ->
                context.startActivity(exportData.intent)
            }
    }

    LaunchedEffect(Unit) {
        viewModel.exportFromDeviceFlow.flowWithLifecycle(lifecycleOwner.lifecycle).mapNotNull { it }
            .collect { exportDataPair ->
                exportData = exportDataPair
            }
    }

    if (exportData != null) {
        ExtractFileInstructionsBottomSheet(textFilePath = exportData!!.first.file.absolutePath,
            jsonFilePath = exportData!!.second.file.absolutePath,
            onDismiss = { exportData = null })
    }
}