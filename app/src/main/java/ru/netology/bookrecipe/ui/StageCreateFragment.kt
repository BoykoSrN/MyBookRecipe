package ru.netology.bookrecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.bookrecipe.databinding.FragmentStageCreateBinding
import ru.netology.bookrecipe.R
import android.net.Uri as Uri1

class StageCreateFragment : Fragment(R.layout.fragment_stage_create) {

    private lateinit var binding: FragmentStageCreateBinding
    private var uriPhoto: Uri1? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStageCreateBinding.inflate(layoutInflater, container, false)

        val imageView = binding.imageStage

        with(binding) {
            saveStage.setOnClickListener { onSaveButtonClicked() }
            imageStage.setOnClickListener {}
        }

        imageView.setOnClickListener {
            result.launch("image/*")
        }

        return binding.root
    }

    private val result =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                uriPhoto = it
                Glide.with(this).load(it).override(1000, 1000).into(binding.imageStage)
            }
        }

    private fun onSaveButtonClicked() {
        val content = binding.textInputLayout.editText?.text
        if (!content.isNullOrBlank()) {
            val resultBundle = Bundle(2)
            resultBundle.putString(STAGE_CONTENT_KEY, content.toString())
            resultBundle.putString(STAGE_URI_PHOTO_KEY, uriPhoto.toString())
            setFragmentResult(ADD_STAGE_REQUEST_KEY, resultBundle)
        }
        val direction =
            StageCreateFragmentDirections.actionStageCreateFragmentToRecipeCreateFragment2()
        findNavController().navigate(direction)
    }

    companion object {
        const val STAGE_CONTENT_KEY = "stageContentKey"
        const val STAGE_URI_PHOTO_KEY = "uriPhotoKey"
        const val ADD_STAGE_REQUEST_KEY = "addStageRequestKey"
    }
}