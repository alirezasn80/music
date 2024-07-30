package ir.flyap.chavoshi.utill

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.StringRes
import ir.flyap.chavoshi.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeout
import java.io.IOException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Keep
data class Response(
    val success: Boolean,
    val message: String,
    val code: Int,
)

sealed interface DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>
    data class Error(val remoteError: RemoteError) : DataState<Nothing>
    object Loading : DataState<Nothing>
}

fun <T> Flow<T>.asDataState(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    duration: Duration = 15.seconds,
): Flow<DataState<T>> {
    return this
        .map<T, DataState<T>> {
            withTimeout(duration) {
                DataState.Success(it)
            }
        }
        .onStart {
            emit(DataState.Loading)
        }
        .catch {
            emit(DataState.Error(it.toRemoteError()))
        }.flowOn(dispatcher)
}

data class RemoteError(@StringRes val message: Int, val code: Int? = null)

fun Throwable.toRemoteError() = when (this) {

    is IOException -> RemoteError(R.string.network_error)

    // is HttpException -> RemoteError(R.string.http_error, this.code())

    is TimeoutCancellationException -> RemoteError(R.string.timeout)

    is NullPointerException -> RemoteError(R.string.network_data_null)

    else -> RemoteError(R.string.unknown_error)

}

fun isOnline(context: Context): Boolean {
    var nc: NetworkCapabilities? = null
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            val networks: Array<Network> = connectivityManager.allNetworks

            var i = 0
            while (i < networks.size && nc == null) {
                nc = connectivityManager.getNetworkCapabilities(networks[i])
                i++
            }
            nc

        }
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

            return true
        }
    }
    return false
}
