package ir.flyap.music_a.cache.datastore

interface DataStore {

    suspend fun showOnboarding(
        key: String,
        value: Boolean,
    )

    suspend fun showOnboarding(
        key: String,
    ): Boolean

    suspend fun setCommentStatus(
        key: String,
        value: String,
    )

    suspend fun getCommentStatus(
        key: String,
    ): String?


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