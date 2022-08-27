package ru.netology.bookrecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.adapter.StagesAdapter
import ru.netology.bookrecipe.dataModels.Categories
import ru.netology.bookrecipe.dataModels.Recipe
import ru.netology.bookrecipe.databinding.FragmentRecipeCreateBinding
import ru.netology.bookrecipe.viewModel.RecipeViewModel

class RecipeCreateFragment : Fragment() {
    private val viewModel by activityViewModels<RecipeViewModel>()

    private var filterList = arrayListOf<Int>()
    lateinit var binding: FragmentRecipeCreateBinding
    private var currentRecipe: Recipe? = null

    private fun onSaveButtonClicked(binding: FragmentRecipeCreateBinding) {
        val text = binding.titleEditText.text
        if (text.isNullOrBlank()) {
            Toast.makeText(context, R.string.no_caption_create_recipe, Toast.LENGTH_SHORT).show()
            return
        }
        if (viewModel.getCurrentRecipe().value?.stages?.size!! < 1) {
            Toast.makeText(context, R.string.no_stage_create_recipe, Toast.LENGTH_SHORT).show()
            return
        }
        if (viewModel.getCurrentRecipe().value?.categories?.size!! < 1) {
            Toast.makeText(context, R.string.no_category_create_recipe, Toast.LENGTH_SHORT).show()
            return
        }
        val resultBundle = Bundle(2)
        resultBundle.putString(TITLE_KEY, text.toString())
        resultBundle.putIntegerArrayList(CATEGORIES_KEY, filterList)
        setFragmentResult(REQUEST_KEY, resultBundle)
        findNavController().navigate(R.id.homeFragment, null, navOptions {
            launchSingleTop = true
            popUpTo(R.id.nav_graph_create_recipe) {
                inclusive = true
            }
        })
    }

    private fun onCancelButtonClicked() {
        findNavController().navigate(R.id.homeFragment)
    }

    private fun setFilter() {
        if (currentRecipe == null) {
            findNavController().navigate(
                R.id.action_recipeCreateFragment2_to_filterSetupFragment
            )
        } else {
            findNavController().navigate(
                R.id.action_recipeCreateFragment2_to_filterSetupFragment,
                bundleOf(FILTER_LIST_KEY to currentRecipe!!.categories)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecipeCreateBinding.inflate(
        layoutInflater, container, false
    ).also {
        binding = it

        val adapter = StagesAdapter(viewModel)
        val stageRecyclerView = binding.stageRecyclerView
        stageRecyclerView.adapter = adapter

        with(binding) {
            titleEditText.addTextChangedListener {
                if (titleEditText.text.isNullOrBlank()) {
                    titleTextView.visibility = View.GONE
                    titleEditText.textSize = 32F
                } else {
                    titleTextView.visibility = View.VISIBLE
                    titleEditText.textSize = 20F
                }
            }

            buttonSave.setOnClickListener { onSaveButtonClicked(binding) }
            buttonCancel.setOnClickListener { onCancelButtonClicked() }
            categoryViewStart.setOnClickListener {
                viewModel.setTitleCurrentRecipe(titleEditText.text.toString())
                setFilter()
            }
            categoryTextViewCaption.setOnClickListener {
                viewModel.setTitleCurrentRecipe(titleEditText.text.toString())
                setFilter()
            }
            categoryTextViewContent.setOnClickListener {
                viewModel.setTitleCurrentRecipe(titleEditText.text.toString())
                setFilter()
            }
            stageAddButton.setOnClickListener {
                viewModel.setTitleCurrentRecipe(titleEditText.text.toString())
                createStage()
            }
        }

        setFragmentResultListener(
            requestKey = StageCreateFragment.ADD_STAGE_REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != StageCreateFragment.ADD_STAGE_REQUEST_KEY) return@setFragmentResultListener
            val content = bundle.getString(
                StageCreateFragment.STAGE_CONTENT_KEY
            ) ?: return@setFragmentResultListener
            val uriPhoto = bundle.getString(StageCreateFragment.STAGE_URI_PHOTO_KEY)
            viewModel.onSaveStageClicked(content = content, uriPhoto = uriPhoto)
        }

        viewModel.getCurrentRecipe().observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                currentRecipe = recipe
                bind(recipe)
                adapter.submitList(recipe.stages)
                adapter.notifyDataSetChanged()
            }
        }
    }.root

    private fun bind(recipe: Recipe?) {
        if (recipe != null) {
            binding.titleEditText.setText(recipe.title)
            binding.categoryTextViewContent.text = getCategoriesText(recipe.categories)
        }
    }

    private fun getCategoriesText(list: List<Int>): String {
        var text =
            if (list.contains(Categories.European.id)) getString(R.string.category_european) + "\n" else ""
        text += if (list.contains(Categories.Asian.id)) getString(R.string.category_asian) + "\n" else ""
        text += if (list.contains(Categories.PanAsian.id)) getString(R.string.category_pan_asian) + "\n" else ""
        text += if (list.contains(Categories.Eastern.id)) getString(R.string.category_eastern) + "\n" else ""
        text += if (list.contains(Categories.American.id)) getString(R.string.category_american) + "\n" else ""
        text += if (list.contains(Categories.Russian.id)) getString(R.string.category_russian) + "\n" else ""
        text += if (list.contains(Categories.Mediterranean.id)) getString(R.string.category_mediterranean) + "\n" else ""
        text = text.removeSuffix("\n")
        return text
    }

    private fun createStage() {
        val direction =
            RecipeCreateFragmentDirections.actionRecipeCreateFragment2ToStageCreateFragment()
        findNavController().navigate(direction)
    }

    companion object {
        const val TITLE_KEY = "titleKey"
        const val CATEGORIES_KEY = "categoriesKey"
        const val REQUEST_KEY = "requestKey"
        const val FILTER_LIST_KEY = "filterListKey"
    }
}