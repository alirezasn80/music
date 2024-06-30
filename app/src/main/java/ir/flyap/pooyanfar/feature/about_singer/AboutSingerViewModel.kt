package ir.flyap.pooyanfar.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.pooyanfar.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}