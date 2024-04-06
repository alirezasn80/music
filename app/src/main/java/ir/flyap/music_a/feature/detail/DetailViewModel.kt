package ir.flyap.music_a.feature.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.utill.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
) : BaseViewModel<DetailState>(DetailState()) {

}