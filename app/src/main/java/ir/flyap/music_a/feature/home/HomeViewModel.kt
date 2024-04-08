package ir.flyap.music_a.feature.home

import android.app.Application
import android.support.v4.media.MediaBrowserCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.flyap.music_a.R
import ir.flyap.music_a.media.K
import ir.flyap.music_a.media.MediaPlayerServiceConnection
import ir.flyap.music_a.media.MediaPlayerServiceConnectionListener
import ir.flyap.music_a.media.currentPosition
import ir.flyap.music_a.media.isPlaying
import ir.flyap.music_a.model.Music
import ir.flyap.music_a.repository.AudioRepository
import ir.flyap.music_a.service.MediaPlayerService
import ir.flyap.music_a.utill.BaseViewModel
import ir.flyap.music_a.utill.debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AudioRepository,
    serviceConnection: MediaPlayerServiceConnection,
    private val context: Application,
) : BaseViewModel<HomeState>(HomeState()) {

    //-----------------------------------------------------------------------------------

    // why use it? -> playlist,...
    private val isConnected = serviceConnection.isConnected
    private val subscriptionCallback = object
        : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            super.onChildrenLoaded(parentId, children)
            debug("onChildren call back")
        }
    }

    //--------------------------------------------------------------------------------

    // use for update ui buttons stop,play and slider current status and changes
    private var currentPlayBackPosition by mutableLongStateOf(0L)
    private var updatePosition = true
    private val playbackState = serviceConnection.playBackState
    private val serviceConnection = serviceConnection.also { updatePlayBack() }

    //--------------------------------------------------------------------------------

    val isPlaying: Boolean get() = playbackState.value?.isPlaying == true

    val currentDuration: Long get() = MediaPlayerService.currentDuration


    init {
        initMediaPlayerServiceConnectionListener()
        getAllMusic()
        getCategories()
        updateSubscribe()
    }

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategories(
                onSuccess = { items ->
                    state.update { it.copy(categories = items) }
                },
                onError = {
                    debug(it.message)
                }
            )

        }
    }


    //what is it? -> playlist,favorites,...
    private fun updateSubscribe() {
        viewModelScope.launch(Dispatchers.IO) {
            isConnected.collect {
                if (it) {
                    val rootMediaId = serviceConnection.rootMediaId
                    serviceConnection.playBackState.value?.apply {
                        currentPlayBackPosition = position
                    }
                    serviceConnection.subscribe(rootMediaId, subscriptionCallback)

                }

            }
        }
    }

    private fun getAllMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMusic(
                onSuccess = { items ->
                    state.update { it.copy(musics = items) }
                },
                onError = {
                    setMessageBySnackbar(R.string.unknown_error)
                }
            )
        }
    }

    // Use for update current audio and clicked audio to play and update ui
    private fun initMediaPlayerServiceConnectionListener() {
        serviceConnection.initListener(
            object : MediaPlayerServiceConnectionListener {
                override fun onAudioChanged(music: Music) {
                    state.update { it.copy(currentMusic = music) }
                }

            }
        )

    }


    fun playAudio(music: Music) {

        // why call it?
        serviceConnection.playAudio(state.value.musics)
        val prevAudio = state.value.currentMusic?.id

        if (music.id == prevAudio) {

            if (isPlaying) {
                serviceConnection.transportControl.pause()
            } else {
                serviceConnection.transportControl.play()
            }

        } else {
            state.update { it.copy(currentMusic = music) }
            // todo(should change media id to uri)
            serviceConnection.transportControl.playFromMediaId(music.uri.toString(), null)
        }


    }

    fun stopPlayBack() {
        serviceConnection.transportControl.stop()
    }

    // 10s move slider forward
    fun fastForward() {
        serviceConnection.fastForward()
    }

    // 10s move slider back
    fun rewind() {
        serviceConnection.rewind()
    }

    fun skipToNext() {
        serviceConnection.skipToNext()
    }

    fun skipToPrevious() {
        serviceConnection.skipToPrevious()
    }

    // manually move slider
    fun seekTo(value: Float) {
        serviceConnection.transportControl.seekTo(
            (currentDuration * value / 100f).toLong()
        )
    }

    // for update ui button stop and play, for update ui slider
    private fun updatePlayBack() {
        viewModelScope.launch {
            val position = playbackState.value?.currentPosition ?: 0

            if (currentPlayBackPosition != position) {
                currentPlayBackPosition = position
            }

            if (currentDuration > 0) {
                state.update { it.copy(currentDuration = currentPlayBackPosition) }
                val currentProgress = (currentPlayBackPosition.toFloat() / currentDuration.toFloat() * 100f)
                state.update { it.copy(currentProgress = currentProgress) }
            }

            delay(K.PLAYBACK_UPDATE_INTERVAL)
            if (updatePosition) {
                updatePlayBack()
            }


        }


    }


    override fun onCleared() {
        super.onCleared()
        serviceConnection.unSubscribe(
            K.MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
        updatePosition = false
    }

    fun onAlbumClick(album: String) {
        debug("album : $album")
        if (album == "همه") {
            getAllMusic()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getMusics(
                album = album,
                onSuccess = { items ->
                    state.update { it.copy(musics = items) }
                },
                onError = {
                    setMessageBySnackbar(R.string.unknown_error)
                }
            )
        }

    }

}