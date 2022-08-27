package ru.netology.bookrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.dataModels.Categories
import ru.netology.bookrecipe.dataModels.Recipe
import ru.netology.bookrecipe.databinding.CardLayoutBinding

internal class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: CardLayoutBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private var itemTextView: TextView = binding.title
        private lateinit var recipe: Recipe


        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.menuButton).apply {
                inflate(R.menu.recipe_menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe.id)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likeButton.setOnClickListener { listener.onLikeClicked(recipe.id) }
            binding.menuButton.setOnClickListener { popupMenu.show() }
            binding.cardView.setOnClickListener { listener.onToRecipe(recipe) }
            binding.authorName.setOnClickListener { listener.onToRecipe(recipe) }
            binding.title.setOnClickListener { listener.onToRecipe(recipe) }
            binding.categoryTextViewCaption.setOnClickListener { listener.onToRecipe(recipe) }
            binding.categoryTextViewContent.setOnClickListener { listener.onToRecipe(recipe) }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                itemTextView.text = recipe.title
                authorName.text =
                    recipe.author
                categoryTextViewContent.text =
                    getCategoriesText(recipe.categories)
                likeButton.isChecked = recipe.liked
            }
        }

        private fun getCategoriesText(list: List<Int>): String {
            var text =
                if (list.contains(Categories.European.id)) itemView.context.getString(R.string.category_european) + "\n" else ""
            text += if (list.contains(Categories.Asian.id)) itemView.context.getString(R.string.category_asian) + "\n" else ""
            text += if (list.contains(Categories.PanAsian.id)) itemView.context.getString(R.string.category_pan_asian) + "\n" else ""
            text += if (list.contains(Categories.Eastern.id)) itemView.context.getString(R.string.category_eastern) + "\n" else ""
            text += if (list.contains(Categories.American.id)) itemView.context.getString(R.string.category_american) + "\n" else ""
            text += if (list.contains(Categories.Russian.id)) itemView.context.getString(R.string.category_russian) + "\n" else ""
            text += if (list.contains(Categories.Mediterranean.id)) itemView.context.getString(R.string.category_mediterranean) + "\n" else ""
            text = text.removeSuffix("\n")
            return text
        }
    }

}

object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
        oldItem == newItem
}