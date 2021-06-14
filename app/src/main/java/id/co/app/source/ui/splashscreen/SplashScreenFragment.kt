package id.co.app.source.ui.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.core.extension.whatIfNotNull
import id.co.app.source.databinding.FragmentSplashScreenBinding
import id.co.app.source.usersession.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
	@Inject
	lateinit var userSession: UserSession

	private val binding by lazy { FragmentSplashScreenBinding.inflate(layoutInflater) }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewLifecycleOwner.lifecycleScope.launch {
			userSession.getUser().collect {
				delay(1500)
				it.whatIfNotNull(
					whatIfNot = {
						findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
					},
					whatIf = {
						findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment())
					}
				)
			}
		}
	}
}