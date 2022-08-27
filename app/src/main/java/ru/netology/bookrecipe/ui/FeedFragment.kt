package ru.netology.bookrecipe.ui

import android.os.Bundle
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.adapter.RecipeAdapter
import ru.netology.bookrecipe.databinding.FragmentFeedBinding
import ru.netology.bookrecipe.viewModel.RecipeViewModel

class FeedFragment : Fragment(), MenuProvider {

    private val viewModel by activityViewModels<RecipeViewModel>()
    private lateinit var binding: FragmentFeedBinding
    private lateinit var adapter: RecipeAdapter
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var emptyView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeedBinding.inflate(
        layoutInflater, container, false
    ).also {
        binding = it

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        adapter = RecipeAdapter(viewModel)
        recipeRecyclerView = binding.recipeRecyclerView
        emptyView = binding.emptyView
        recipeRecyclerView.adapter = adapter

        setFragmentResultListener(
            requestKey = RecipeCreateFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != RecipeCreateFragment.REQUEST_KEY) return@setFragmentResultListener
            val titleRecipe = bundle.getString(
                RecipeCreateFragment.TITLE_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveRecipe(titleRecipe)
        }

        viewModel.navigateToRecipeEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_feedFragment3_to_recipeViewFragment)
        }

        viewModel.navigateToRecipeEditorScreenEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_feedFragment3_to_nav_graph_create_recipe3)
        }

        viewModel.filtratedData.observe(viewLifecycleOwner) { recipes ->
            emptyData(recipes.size)
            adapter.submitList(recipes)
        }

        binding.buttonAddRecipe.setOnClickListener {
            viewModel.onInsertClicked()
            findNavController().navigate(R.id.action_feedFragment3_to_nav_graph_create_recipe3)
        }

    }.root

    private fun emptyData(sizeList: Int) {
        if (sizeList == 0) {
            recipeRecyclerView.visibility = INVISIBLE
            emptyView.visibility = VISIBLE
        } else {
            recipeRecyclerView.visibility = VISIBLE
            emptyView.visibility = INVISIBLE
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return if (menuItem.itemId == R.id.search) {
            val searchView: SearchView = menuItem.actionView as SearchView
            searchView.imeOptions = EditorInfo.IME_ACTION_DONE
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchQuery.value = newText
                    return true
                }
            })
            true
        } else false
    }

}
