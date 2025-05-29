package rahul.lohra.networkmonitor.presentation.ui

import kotlinx.coroutines.flow.Flow
import rahul.lohra.networkmonitor.domain.usecas.ExportData

interface SharableDeletable {
    val shareIntentFlow : Flow<ExportData>
    val exportFromDeviceFlow : Flow<Pair<ExportData, ExportData>>
}
