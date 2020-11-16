package id.co.app.source.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.co.app.source.databinding.FragmentFeedBinding
import id.co.app.source.databinding.FragmentFeedBindingImpl

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBindingImpl.inflate(inflater)
        return binding.root
    }

}