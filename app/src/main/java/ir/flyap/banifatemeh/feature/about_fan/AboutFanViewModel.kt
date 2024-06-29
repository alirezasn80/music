package ir.flyap.banifatemeh.feature.about_fan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.banifatemeh.api.service.ApiService
import ir.flyap.banifatemeh.utill.Arg
import ir.flyap.banifatemeh.utill.BaseViewModel
import ir.flyap.banifatemeh.utill.debug
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutFanViewModel @Inject constructor(
    private val savedstate: SavedStateHandle,
    private val apiService: ApiService
) : BaseViewModel<AboutFanState>(AboutFanState()) {
    val id = savedstate.get<String>(Arg.FAN_ID)!!.toInt()

    init {
        debug("id : $id")
        getInfo()
    }

    private fun getInfo() {

        viewModelScope.launch(IO) {
            try {
                val data = apiService.getFanInfo(id)
                state.update { it.copy(data = data) }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                state.update { it.copy(isLoading = false) }

            }
        }
    }
}