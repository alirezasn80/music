package ir.flyap.banifatemeh.feature.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.banifatemeh.utill.BaseViewModel
import ir.flyap.banifatemeh.utill.Destination
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