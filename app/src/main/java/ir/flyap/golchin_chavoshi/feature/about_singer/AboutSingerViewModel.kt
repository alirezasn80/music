package ir.flyap.golchin_chavoshi.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.golchin_chavoshi.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}