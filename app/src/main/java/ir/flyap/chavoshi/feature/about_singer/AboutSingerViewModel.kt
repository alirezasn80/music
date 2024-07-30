package ir.flyap.chavoshi.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.chavoshi.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}