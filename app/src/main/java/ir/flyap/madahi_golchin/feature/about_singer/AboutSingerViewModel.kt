package ir.flyap.madahi_golchin.feature.about_singer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.madahi_golchin.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSingerViewModel @Inject constructor(
) : BaseViewModel<AboutSingerState>(AboutSingerState()) {

}