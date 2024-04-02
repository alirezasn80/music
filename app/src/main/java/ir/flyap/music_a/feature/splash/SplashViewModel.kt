package ir.flyap.music_a.feature.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : BaseViewModel<SplashState>(SplashState()) {

}