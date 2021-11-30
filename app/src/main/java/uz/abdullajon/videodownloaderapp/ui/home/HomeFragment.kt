package uz.abdullajon.videodownloaderapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.downloader.PRDownloader
import dagger.hilt.android.AndroidEntryPoint
import uz.abdullajon.videodownloaderapp.data.model.VideoDownloadModel
import uz.abdullajon.videodownloaderapp.data.model.VideoModel
import uz.abdullajon.videodownloaderapp.databinding.FragmentHomeBinding
import uz.abdullajon.videodownloaderapp.util.*
import java.io.File

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var dirPath: String
    private lateinit var adapter: VideoDownloaderAdapter
    private val downloadVideos = ArrayList<VideoDownloadModel>()
    lateinit var myVideoDownloader: MyVideoDownloader


    private val binding get() = _binding!!
    val VIDEO_URL = "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dirPath = requireContext().cacheDir.toString()
        myVideoDownloader = MyVideoDownloader(requireContext())
        binding.etUrl.setText(VIDEO_URL)
        loadAction()
        loadView()
    }

    private fun loadView() {
        adapter = VideoDownloaderAdapter(this::pauseItemClick, this::stopItemClick)
        binding.rvVideos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideos.adapter = adapter
    }

    private fun pauseItemClick(videoDownloadModel: VideoDownloadModel, position: Int) {
        Log.d("YYYY", videoDownloadModel.id.toString())
        if (videoDownloadModel.downloadStatus == DOWNLOAD_STATUS_PAUSE) {
            PRDownloader.resume(videoDownloadModel.id)
            downloadVideos[position] =
                videoDownloadModel.copy(downloadStatus = DOWNLOAD_STATUS_RESUME)
        } else {
            downloadVideos[position] =
                videoDownloadModel.copy(downloadStatus = DOWNLOAD_STATUS_PAUSE)
            PRDownloader.pause(videoDownloadModel.id)
        }
        adapter.notifyItemChanged(position)
    }

    private fun stopItemClick(videoDownloadModel: VideoDownloadModel) {
        downloadVideos.remove(videoDownloadModel)
        adapter.submitList(downloadVideos)
        //should remove
        adapter.notifyDataSetChanged()
        PRDownloader.cancel(videoDownloadModel.id)
    }


    private fun loadAction() {
        binding.btnDownload.setOnClickListener {
            downloadItem(binding.etUrl.text?.trim().toString())
        }
    }

    private fun downloadItem(url: String) {
        val index: Int = url.lastIndexOf("/")
        val fileName = url.substring(index + 1)
        val file = File(requireContext().cacheDir, fileName)
        if (!file.exists()) {
            Log.d("AAAAA", url.last4Element())
            when (val sURL = filterMP4(url)) {
                "EMPTY TEXT" -> {
                    showToast(sURL)
                }
                "Enter MP4 file" -> {
                    showToast(sURL)
                }
                else -> {
                    getVideoDownloader(sURL)
                }
            }
        } else {
            showToast("File already added")
        }
    }

    private fun getVideoDownloader(url: String) {
        myVideoDownloader.download(
            url,
            requireContext().cacheDir.toString()
        ) { videoDownloadModel ->

//            requireContext().setNotificationVideo(url, videoDownloadModel)
            Log.d("YYYY", videoDownloadModel.toString())

            downloadVideos.filter { it.id == videoDownloadModel.id }.forEach {
                downloadVideos.remove(it)
            }
//            if (downloadVideos.map { it.id }.contains(videoDownloadModel.id)) {
//                downloadVideos.remove(videoDownloadModel)
//            }
            downloadVideos.add(videoDownloadModel)
            adapter.submitList(downloadVideos)
            //should remove
            adapter.notifyDataSetChanged()

            Log.d("PPPPP", videoDownloadModel.toString())

            if (videoDownloadModel.downloadStatus == DOWNLOAD_STATUS_COMPLETED) {

                showToast("download compleated")

//                val videoModel = VideoModel(
//                    videoDownloadModel.title,
//                    videoDownloadModel.url,
//                    videoDownloadModel.totalSizeTxt
//                )
                if (downloadVideos.map { it.id }.contains(videoDownloadModel.id)) {
                    downloadVideos.remove(videoDownloadModel)
                }
                adapter.submitList(downloadVideos)
                adapter.notifyDataSetChanged()
            }
            if (videoDownloadModel.downloadStatus == DOWNLOAD_STATUS_FAILED) {
                showToast(videoDownloadModel.downloadFailedMessage)

                if (downloadVideos.map { it.id }.contains(videoDownloadModel.id)) {
                    downloadVideos.remove(videoDownloadModel)
                }
                adapter.submitList(downloadVideos)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}