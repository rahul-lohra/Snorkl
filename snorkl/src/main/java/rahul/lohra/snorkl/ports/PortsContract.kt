package rahul.lohra.snorkl.ports

interface PortsContract {
    fun resetPort()

    fun getLastUsedPort(): Int

    fun getNewPort(): Int

    fun findAvailablePort(forceRefresh: Boolean = false): Int
}