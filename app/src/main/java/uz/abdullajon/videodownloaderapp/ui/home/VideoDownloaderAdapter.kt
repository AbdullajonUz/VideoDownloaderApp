package uz.abdullajon.videodownloaderapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.abdullajon.videodownloaderapp.data.model.VideoDownloadModel
import uz.abdullajon.videodownloaderapp.databinding.ItemVideoDownloadBinding
import uz.abdullajon.videodownloaderapp.util.DOWNLOAD_STATUS_PAUSE
import uz.abdullajon.videodownloaderapp.util.DOWNLOAD_STATUS_RESUME
import uz.abdullajon.videodownloaderapp.util.DoubleBlock
import uz.abdullajon.videodownloaderapp.util.SingleBlock

class VideoDownloaderAdapter(
    private val pauseItemClick: DoubleBlock<VideoDownloadModel,Int>,
    private val stopItemClick: SingleBlock<VideoDownloadModel>
) :
    ListAdapter<VideoDownloadModel, VideoDownloaderAdapter.VH>(diffUtil) {

    inner class VH(val itemDownloadBinding: ItemVideoDownloadBinding) :
        RecyclerView.ViewHolder(itemDownloadBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemVideoDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = currentList[position]

        holder.itemDownloadBinding.videoName.text = data.title
        holder.itemDownloadBinding.downloadProgress.text = "${data.downloadPercent} %"
        if (data.downloadStatus == DOWNLOAD_STATUS_RESUME) {
            holder.itemDownloadBinding.pauseBtn.text = "PASUE"
        } else if (data.downloadStatus == DOWNLOAD_STATUS_PAUSE) {
            holder.itemDownloadBinding.pauseBtn.text = "RESUME"
        }

        holder.itemDownloadBinding.pauseBtn.setOnClickListener {
            pauseItemClick.invoke(data,position)
        }
        holder.itemDownloadBinding.stopBtn.setOnClickListener {
            stopItemClick.invoke(data)
        }
    }

}

private val diffUtil = object : DiffUtil.ItemCallback<VideoDownloadModel>() {
    override fun areItemsTheSame(
        oldItem: VideoDownloadModel,
        newItem: VideoDownloadModel
    ): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(
        oldItem: VideoDownloadModel,
        newItem: VideoDownloadModel
    ): Boolean =
        oldItem == newItem
}