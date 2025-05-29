package rahul.lohra.snorkl.notification

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import rahul.lohra.snorkl.data.local.db.DatabaseProvider
import rahul.lohra.snorkl.data.local.entities.NetworkType
import java.util.Locale

object NotificationSdkInitializer {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val listOfText = FixedSizeQueue<String>(5)

    fun initialize(context: Context) {
        scope.launch {
            DatabaseProvider.getDatabase()
                .networkLogDao().getLatestNetworkLog().mapNotNull { it }.collect {

                    when(it.networkType) {
                        NetworkType.REST.title -> {
                            val uri = Uri.parse(it.requestUrl)
                            val formattedText = "${it.responseCode} ${it.method?.capitalize(Locale.US)} ${uri.path}"
                            listOfText.add(formattedText)
                        }
                        NetworkType.WEBSOCKET.title -> {
//                            val uri = Uri.parse(it.requestUrl)
//                            val formattedText = "${it.responseCode} ${it.method?.capitalize(Locale.US)} ${uri.path}"
//                            listOfText.add(formattedText
                        }
                    }
                    val notificationDescriptionText = listOfText.toList().joinToString("\n")
                    NetworkLogNotifier.showNotification(context, notificationDescriptionText)
                }
        }
    }
}

class FixedSizeQueue<T>(private val maxSize: Int) {
    private val deque = ArrayDeque<T>()

    fun add(item: T) {
        if (deque.size >= maxSize) deque.removeLast()
        deque.addFirst(item)
    }

    fun toList(): List<T> = deque.toList()
}
