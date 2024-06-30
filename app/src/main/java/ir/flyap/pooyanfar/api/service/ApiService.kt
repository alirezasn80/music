package ir.flyap.pooyanfar.api.service

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

@Keep
data class FanModel(
    @SerializedName("data")
    val data: List<FanItem>
)

@Keep
data class FanItem(
    @SerializedName("fan_name")
    val fanName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("profile")
    val profile: String
)

@Keep
data class FanInfoModel(
    val id: Int,
    @SerializedName("fan_name")
    val name: String,
    val profile: String,
    val instagram: String?,
    val telegram: String?,
    val description: String
)


interface ApiService {
    @GET("fanpages")
    suspend fun getFans(): FanModel

    @GET("fanpages_info/{id}")
    suspend fun getFanInfo(@Path("id") id: Int): FanInfoModel
}