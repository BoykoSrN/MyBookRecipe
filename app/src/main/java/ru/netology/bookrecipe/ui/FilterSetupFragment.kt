package ru.netology.bookrecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.*
import ru.netology.bookrecipe.databinding.FilterFragmentSwitchBinding
import ru.netology.bookrecipe.R
import ru.netology.bookrecipe.dataModels.Categories
import ru.netology.bookrecipe.viewModel.RecipeViewModel
import java.util.ArrayList

class FilterSetupFragment : Fragment(R.layout.filter_fragment_switch) {

    private lateinit var binding: FilterFragmentSwitchBinding
    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FilterFragmentSwitchBinding.inflate(
        layoutInflater, container, false
    ).also {
        binding = it

        val filterList = arguments?.getIntegerArrayList(RecipeCreateFragment.FILTER_LIST_KEY)

        if (filterList != null) {
            bind(filterList)
        }
    }.root

    private fun bind(categories: ArrayList<Int>) {
        with(binding) {
            switchEuropean.isChecked = categories.contains(Categories.European.id)
            switchAsian.isChecked = (categories.contains(Categories.Asian.id))
            switchPanAsian.isChecked = categories.contains(Categories.PanAsian.id)
            switchEastern.isChecked = categories.contains(Categories.Eastern.id)
            switchAmerican.isChecked = categories.contains(Categories.American.id)
            switchRussian.isChecked = categories.contains(Categories.Russian.id)
            switchMediterranean.isChecked = categories.contains(Categories.Mediterranean.id)
        }
    }

    private fun getSwitchSettings(): List<Int> {
        val list = mutableListOf<Int>()
        with(binding) {
            if (switchEuropean.isChecked) list.add(Categories.European.id)
            if (switchAsian.isChecked) list.add(Categories.Asian.id)
            if (switchPanAsian.isChecked) list.add(Categories.PanAsian.id)
            if (switchEastern.isChecked) list.add(Categories.Eastern.id)
            if (switchAmerican.isChecked) list.add(Categories.American.id)
            if (switchRussian.isChecked) list.add(Categories.Russian.id)
            if (switchMediterranean.isChecked) list.add(Categories.Mediterranean.id)
        }
        return list
    }

    override fun onDestroy() {
        viewModel.setupSwitchSettingsCurrentRecipe(getSwitchSettings())
        super.onDestroy()
    }

}