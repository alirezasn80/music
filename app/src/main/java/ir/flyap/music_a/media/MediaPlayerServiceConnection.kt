package ir.flyap.music_a.media

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.flyap.music_a.model.Audio
import ir.flyap.music_a.service.MediaPlayerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface MediaPlayerServiceConnectionListener {
    fun onAudioChanged(audio: Audio)
}

class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {
    private lateinit var _listener: MediaPlayerServiceConnectionListener

    fun initListener(listener: MediaPlayerServiceConnectionListener) {
        this._listener = listener
    }

    private val _playBackState: MutableStateFlow<PlaybackStateCompat?> =
        MutableStateFlow(null)
    val playBackState: StateFlow<PlaybackStateCompat?>
        get() = _playBackState

    private val _isConnected: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected


    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback =
        MediaBrowserConnectionCallBack(context)
    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlayerService::class.java),
        mediaBrowserServiceCallback,
        null

    ).apply {
        connect()
    }
    private var audioList = listOf<Audio>()

    val rootMediaId: String
        get() = mediaBrowser.root

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls


    fun playAudio(audios: List<Audio>) {
        audioList = audios
        mediaBrowser.sendCustomAction(K.START_MEDIA_PLAY_ACTION, null, null)
    }

    fun fastForward(seconds: Int = 10) {
        playBackState.value?.currentPosition?.let {
            transportControl.seekTo(it + seconds * 1000)
        }
    }

    fun rewind(seconds: Int = 10) {
        playBackState.value?.currentPosition?.let {
            transportControl.seekTo(it - seconds * 1000)
        }
    }

    fun skipToNext() {
        transportControl.skipToNext()
    }

    fun skipToPrevious() {
        transportControl.skipToPrevious()
    }

    fun subscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.subscribe(parentId, callBack)
    }

    fun unSubscribe(
        parentId: String,
        callBack: MediaBrowserCompat.SubscriptionCallback
    ) {
        mediaBrowser.unsubscribe(parentId, callBack)
    }

    fun refreshMediaBrowserChildren() {
        mediaBrowser.sendCustomAction(
            K.REFRESH_MEDIA_PLAY_ACTION,
            null,
            null
        )
    }

    private inner class MediaBrowserConnectionCallBack(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
                registerCallback(MediaControllerCallBack())
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    private inner class MediaControllerCallBack : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)

            metadata?.let { data ->
                val currentAudio = audioList.find { it.uri == data.description.mediaUri }
                currentAudio?.let { _listener.onAudioChanged(it) }
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            mediaBrowserServiceCallback.onConnectionSuspended()
        }


    }

}