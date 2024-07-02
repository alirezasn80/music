package ir.flyap.madahi_golchin.cache.datastore

interface DataStore {

    suspend fun showOnboarding(
        key: String,
        value: Boolean,
    )

    suspend fun showOnboarding(
        key: String,
    ): Boolean

    suspend fun setHideAskComment(
        key: String,
        value: Boolean,
    )

    suspend fun hideAskComment(
        key: String,
    ): Boolean


    suspend fun setOpenAppCounter(
        key: String,
        value: Int,
    )

    suspend fun getOpenAppCounter(
        key: String,
    ): Int

    suspend fun validPermission(
        key: String,
        value: Boolean,
    )

    suspend fun isValidPermission(
        key: String,
    ): Boolean

    suspend fun isVIP(
        key: String,
        value: Boolean,
    )

    suspend fun isVIP(
        key: String
    ): Boolean?


    suspend fun clear()

}