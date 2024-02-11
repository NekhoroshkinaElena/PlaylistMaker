import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitRequester
import com.example.playlistmaker.data.player.TrackPlayerImpl
import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.TrackPlayer
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TrackStorage
import com.example.playlistmaker.domain.impl.TrackPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.Track

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TracksRepositoryImpl(RetrofitRequester())
    }

    fun provideTracksInteractor(trackStorage: TrackStorage): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository(), trackStorage)
    }

    private fun getTrackPlayer(track: Track?): TrackPlayer {
        return TrackPlayerImpl(track)
    }

    fun provideTrackPlayer(track: Track?): MediaPlayerInteractor {
        return TrackPlayerInteractorImpl(getTrackPlayer(track))
    }
}
