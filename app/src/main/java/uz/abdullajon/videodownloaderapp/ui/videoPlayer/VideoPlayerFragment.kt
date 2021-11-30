package uz.abdullajon.videodownloaderapp.ui.videoPlayer

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import uz.abdullajon.videodownloaderapp.data.model.VideoModel
import uz.abdullajon.videodownloaderapp.databinding.FragmentVideoPlayerBinding

class VideoPlayerFragment : Fragment() {
    private var _binding: FragmentVideoPlayerBinding? = null

    private lateinit var player: ExoPlayer

    private val binding get() = _binding!!
    private var data: VideoModel? = null
    private var position = 1
    private var allVideos: ArrayList<VideoModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = requireArguments().getInt("position", 1)
        data = requireArguments().getParcelable<VideoModel>("data")
        allVideos = requireArguments().getParcelableArrayList<VideoModel>("allVideos")
        if (data != null) {
            playVideo()
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (::player.isInitialized) {
                    player.stop()
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    private fun playVideo() {
        Log.d("BBBBBB", allVideos.toString())
        Log.d("BBBBBB", data.toString())
        Log.d("BBBBBB", position.toString())

        player = ExoPlayer.Builder(requireContext()).build()
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), "app"))

        val concatenatingMediaSource = ConcatenatingMediaSource()
        allVideos?.forEach {
            val mediaSource = ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(Uri.parse(it.localURL))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        binding.videoView.player = player
        binding.videoView.keepScreenOn = true
        player.prepare(concatenatingMediaSource)
        player.seekTo(position, C.TIME_UNSET)
        playError()
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
        player.playbackState
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
        player.playbackState
    }

    private fun playError() {
        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: PlaybackException) {
                Log.d("BBBBB", "xabar")
                Toast.makeText(requireContext(), "error Play Video", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == PlaybackStateCompat.STATE_REWINDING) {
                    try {
                        player.stop()
                        position--
                        playVideo()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "error Play Video", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else if (playbackState == PlaybackStateCompat.STATE_FAST_FORWARDING) {
                    try {
                        player.stop()
                        position++
                        playVideo()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "error Play Video", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        player.playWhenReady = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}