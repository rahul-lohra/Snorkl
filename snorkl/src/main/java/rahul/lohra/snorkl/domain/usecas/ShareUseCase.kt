package rahul.lohra.snorkl.domain.usecas

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import rahul.lohra.snorkl.core.SdkContextHolder
import rahul.lohra.snorkl.data.local.entities.NetworkEntity
import rahul.lohra.snorkl.data.repository.NetworkRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShareUseCase(private val repository: NetworkRepository) {

    suspend fun shareAllNetworkLogs(asJson: Boolean): ExportData {
        val logs = repository.getAllLogs()
        return shareLogs(logs, asJson)
    }

    suspend fun shareNetworkLog(id: String, asJson: Boolean): ExportData {
        val log = repository.getLog(id).first()
        return shareLogs(listOf(log), asJson)
    }

    private suspend fun shareLogs(logs: List<NetworkEntity>, asJson: Boolean): ExportData {
        val context = SdkContextHolder.getContext()
        val ext = if (asJson) "json" else "text"
        val text = if (asJson) convertLogsToJson(logs) else convertLogsToText(logs)
        val file = saveTextToFile(context, "network_logs", ext, text)
        return ExportData(shareFile(context, file), file)
    }


    private fun convertLogsToText(logs: List<NetworkEntity>): String {
        return logs.joinToString(separator = "\n\n") { log ->
            """
        |Method: ${log.method}
        |URL: ${log.requestUrl}
        |Status: ${log.responseCode}
        |Request: ${log.requestBody}
        |Response: ${log.body}
        |Timestamp: ${Date(log.timestamp)}
        |Type: ${log.networkType}
        """.trimMargin()
        }
    }

    private fun saveTextToFile(
        context: Context,
        baseFileName: String,
        ext: String,
        text: String
    ): File {
        val timestamp = generateTimestamp()
        val fileName = "${baseFileName}_$timestamp.$ext"
        val file = File(context.cacheDir, fileName)
        file.writeText(text)
        return file
    }

    private fun shareFile(context: Context, file: File): Intent {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return Intent.createChooser(intent, "Share Network Logs")
    }

    private fun convertLogsToJson(logs: List<NetworkEntity>): String {
        val gson = Gson()
        return gson.toJson(logs)
    }


    private fun generateTimestamp(): String {
        val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return formatter.format(Date())
    }

}

data class ExportData(val intent: Intent, val file: File)
