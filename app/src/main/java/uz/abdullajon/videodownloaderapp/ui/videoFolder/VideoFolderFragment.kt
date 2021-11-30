package uz.abdullajon.videodownloaderapp.ui.videoFolder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.abdullajon.videodownloaderapp.R
import uz.abdullajon.videodownloaderapp.data.model.VideoModel
import uz.abdullajon.videodownloaderapp.databinding.FragmentVideoFolderBinding
import uz.abdullajon.videodownloaderapp.util.getSize
import java.io.File


@AndroidEntryPoint
class VideoFolderFragment : Fragment() {

    private var _binding: FragmentVideoFolderBinding? = null


    private lateinit var adapter: VideoFilesAdapter
    private val allVideos = ArrayList<VideoModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoFolderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadView()
        allVideos.clear()
        allVideos.addAll(allMedia())
        adapter.submitList(allVideos)
    }

    private fun loadView() {
        adapter = VideoFilesAdapter(requireContext(), this::itemClick)
        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideos.adapter = adapter
    }

    private fun itemClick(position: Int, videoModel: VideoModel) {
        findNavController().navigate(
            R.id.action_nav_videos_to_nav_video_player,
            bundleOf(
                "data" to videoModel,
                "position" to position,
                "allVideos" to allVideos
            )
        )
    }


    private fun allMedia(): ArrayList<VideoModel> {
        val videoFiles = ArrayList<VideoModel>()
        val file = File(requireContext().cacheDir.absolutePath)
        if (file.exists()) {
            file.listFiles()?.forEach {
                Log.d("TTT", it.name)
                Log.d("TTT", it.absolutePath)
                val videoModel =
                    VideoModel(it.absolutePath, it.name, getSize(it.length().toInt()))
                videoFiles.add(videoModel)
            }
        }
        return videoFiles
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}