package id.co.app.source.ui.home.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.co.app.source.databinding.FragmentHomeDetailsBinding

class HomeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHomeDetailsBinding
    private lateinit var cameraStr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    private fun loadArguments() {
        arguments?.getString("PHOTO_NAME")?.let {
            cameraStr = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDetailsBinding.inflate(inflater)
        binding.camera = cameraStr
        return binding.root
    }
}
