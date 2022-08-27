package ru.netology.bookrecipe.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.dataModels.Stage
import ru.netology.bookrecipe.databinding.CardStageLayoutBinding

internal class StagesAdapter(
    private val interactionListener: StageInteractionListener
) : ListAdapter<Stage, StagesAdapter.ViewHolder>(DiffCallbackStage) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardStageLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: CardStageLayoutBinding,
        listener: StageInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var stage: Stage

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menuStageButton).apply {
                inflate(R.menu.stage_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.up -> {
                            listener.onMoveStageUpClicked(layoutPosition)
                            true
                        }
                        R.id.down -> {
                            listener.onMoveStageDownClicked(layoutPosition)
                            true
                        }
                        R.id.remove -> {
                            listener.onRemoveStageClicked(stage)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.menuStageButton.setOnClickListener { popupMenu.show() }
        }

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

private object DiffCallbackStage : DiffUtil.ItemCallback<Stage>() {

    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem.content == newItem.content

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem == newItem
}