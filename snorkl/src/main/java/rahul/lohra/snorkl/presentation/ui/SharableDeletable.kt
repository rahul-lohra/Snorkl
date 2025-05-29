package rahul.lohra.snorkl.presentation.ui

import kotlinx.coroutines.flow.Flow
import rahul.lohra.snorkl.domain.usecas.ExportData

interface SharableDeletable {
    val shareIntentFlow : Flow<ExportData>
    val exportFromDeviceFlow : Flow<Pair<ExportData, ExportData>>
}
