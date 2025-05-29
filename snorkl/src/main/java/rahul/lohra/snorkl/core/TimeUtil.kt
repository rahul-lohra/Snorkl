package rahul.lohra.snorkl.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {
    fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }
}