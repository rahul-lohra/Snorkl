package rahul.lohra.snorkl.initializer

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import rahul.lohra.snorkl.core.SdkContextHolder
import rahul.lohra.snorkl.notification.NetworkLogNotifier

class SdkInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        context?.let {
            SdkContextHolder.init(it)
            NetworkLogNotifier.setup(it)
        }
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
