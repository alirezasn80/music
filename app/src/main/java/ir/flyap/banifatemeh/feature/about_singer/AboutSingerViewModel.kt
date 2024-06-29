package ir.flyap.banifatemeh.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.banifatemeh.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}