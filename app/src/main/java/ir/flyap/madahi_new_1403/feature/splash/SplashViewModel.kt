package ir.flyap.madahi_new_1403.feature.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.madahi_new_1403.utill.BaseViewModel
import ir.flyap.madahi_new_1403.utill.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashState>(SplashState()) {

    init {
        viewModelScope.launch {
            delay(1000)
            setDestination(Destination.Home)
        }
    }
}