package ir.flyap.music_a.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}