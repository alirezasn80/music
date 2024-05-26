package ir.flyap.music_a.feature.about_fan

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutFanViewModel @Inject constructor(
) : BaseViewModel<AboutFanState>(AboutFanState()) {

}