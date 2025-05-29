package rahul.lohra.snorkl.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.mapNotNull
import rahul.lohra.snorkl.domain.usecas.ExportData
import rahul.lohra.snorkl.presentation.ui.home.ExtractFileInstructionsBottomSheet

@Composable
fun ShareIntentObserver(sharableDeletable: SharableDeletable) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var exportData by remember { mutableStateOf<Pair<ExportData, ExportData>?>(null) }

    LaunchedEffect(Unit) {
        sharableDeletable.shareIntentFlow.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { exportData ->
                context.startActivity(exportData.intent)
            }
    }

    LaunchedEffect(Unit) {
        sharableDeletable.exportFromDeviceFlow.flowWithLifecycle(lifecycleOwner.lifecycle).mapNotNull { it }
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