package rahul.lohra.snorkl

import rahul.lohra.snorkl.data.local.keyvalue.KeyValueStorage
import rahul.lohra.snorkl.data.local.keyvalue.SnorklSharedPref
import rahul.lohra.snorkl.ports.PortsContract
import java.net.ServerSocket

class PortManager(private val keyValueStorage: KeyValueStorage) : PortsContract {

    companion object {
        const val PORT_NOT_PICKED = -1
    }

    override fun resetPort() {
        keyValueStorage.putInt(SnorklSharedPref.PrefKeys.PORT, PORT_NOT_PICKED)
    }

    override fun getLastUsedPort(): Int {
        val getLastUsedPort =
            keyValueStorage.getInt(SnorklSharedPref.PrefKeys.PORT, PORT_NOT_PICKED)
        return getLastUsedPort
    }

    override fun getNewPort(): Int {
        ServerSocket(0).use { socket ->
            return socket.localPort
        }
    }

    override fun findAvailablePort(forceRefresh: Boolean): Int {

        if (forceRefresh) {
            resetPort()
        }

        var localPort = getLastUsedPort()
        if (localPort == PORT_NOT_PICKED) {
            localPort = getNewPort()
        } else {
            ServerSocket(localPort).use { socket ->
                localPort = socket.localPort
            }
        }

        keyValueStorage.putInt(SnorklSharedPref.PrefKeys.PORT, localPort)
        return localPort
    }

}