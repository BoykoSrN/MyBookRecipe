package ru.netology.bookrecipe.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.bookrecipe.dataModels.Stage
import ru.netology.bookrecipe.databinding.CardStageViewLayoutBinding

internal class ViewStagesAdapter: ListAdapter<Stage, ViewStagesAdapter.ViewHolder>(DiffCallbackStageView) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStageViewLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: CardStageViewLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var stage: Stage

        @SuppressLint("SetTextI18n")
        fun bind(stage: Stage) {
            this.stage = stage
            with(binding) {
                val positionStage = layoutPosition + 1
                textViewStageContent.text = stage.content
                textViewCaptionStage.text = "Шаг $positionStage"
                if (stage.uriPhoto != null) {
                    imageStage.visibility = View.VISIBLE
                    Glide.with(imageStage).load(stage.uriPhoto).override(1000, 1000)
                        .into(imageStage)
                } else {
                    imageStage.visibility = View.GONE
                }
            }
        }
    }
}

private object DiffCallbackStageView : DiffUtil.ItemCallback<Stage>() {

    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem.content == newItem.content

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem == newItem
}