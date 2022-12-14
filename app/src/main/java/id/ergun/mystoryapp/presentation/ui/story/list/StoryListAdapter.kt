package id.ergun.mystoryapp.presentation.ui.story.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.Helper.fromHtml
import id.ergun.mystoryapp.common.util.Helper.loadImage
import id.ergun.mystoryapp.common.util.Helper.toLocalDateFormat
import id.ergun.mystoryapp.databinding.ItemStoryBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel

/**
 * @author erikgunawan
 * Created 02/10/22 at 01.25
 */
class StoryListAdapter :
    PagingDataAdapter<StoryDataModel, StoryListAdapter.ViewHolder>(DiffCallback) {

    var itemClickListener: ((view: View, model: StoryDataModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)

        val binding = ItemStoryBinding.bind(view)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            val item = getItem(position) ?: return
            holder.bind(item)
        }
    }

    inner class ViewHolder(
        private val binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StoryDataModel) {
            binding.run {
                tvDate.text = item.createdAt.toLocalDateFormat()
                tvNameDesc.text = tvNameDesc.context.getString(R.string.name_desc_string_format, item.name, item.description).fromHtml()
                ivPhoto.loadImage(item.photoUrl)
            }

            itemView.setOnClickListener {
                itemClickListener?.invoke(binding.ivPhoto, item)
            }
        }
    }

    companion object {

        object DiffCallback : DiffUtil.ItemCallback<StoryDataModel>() {
            override fun areItemsTheSame(
                oldItem: StoryDataModel,
                newItem: StoryDataModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoryDataModel,
                newItem: StoryDataModel
            ) = oldItem == newItem
        }
    }

}