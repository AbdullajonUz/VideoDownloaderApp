package uz.abdullajon.videodownloaderapp.ui.videoFolder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.abdullajon.videodownloaderapp.data.model.VideoModel
import uz.abdullajon.videodownloaderapp.databinding.ItemVideosBinding
import uz.abdullajon.videodownloaderapp.util.DoubleBlock

class VideoFilesAdapter(
    private val context: Context,
    private val itemClick: DoubleBlock<Int, VideoModel>
) :
    ListAdapter<VideoModel, VideoFilesAdapter.VH>(diffUtil) {

    inner class VH(val videFilesBinding: ItemVideosBinding) :
        RecyclerView.ViewHolder(videFilesBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemVideosBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = currentList[position]


        holder.videFilesBinding.apply {
            videoName.text = data.title
            videoSize.text = data.totalSpace
        }
        holder.videFilesBinding.mainContainer.setOnClickListener {
            itemClick(position, data)
        }
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
        false


    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
        oldItem == newItem
}