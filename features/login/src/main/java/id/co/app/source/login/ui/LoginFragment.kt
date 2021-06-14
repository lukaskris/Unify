package id.co.app.source.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.forestry.plantation.core.extension.closeKeyboard
import com.forestry.plantation.core.extension.showErrorToast
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.source.login.R
import id.co.app.source.login.databinding.FragmentLoginBinding
import id.co.app.source.login.viewmodel.LoginViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {

	private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }

	private val viewModel: LoginViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupView()
		setupData()
		observeLiveData()
	}

	private fun setupView() {
		setupLoginButton()
	}

	private fun setupLoginButton() {
		binding.loginButton.setOnClickListener {
			val username = (binding.username.editText?.text ?: "").toString()
			val password = (binding.password.editText?.text ?: "").toString()
			val location = (binding.region.editText?.text ?: "").toString()
			when {
				username.isBlank() -> {
					context?.showErrorToast(getString(R.string.error_empty_username))
					binding.username.isFocusable = true
				}
				password.isBlank() -> {
					context?.showErrorToast(getString(R.string.error_empty_password))
					binding.password.isFocusable = true
				}
				location.isBlank() -> {
					context?.showErrorToast(getString(R.string.error_empty_location))
					binding.region.isFocusable = true
				}
				else -> {
					activity?.closeKeyboard(it)
					viewModel.login(username, password, location)
				}
			}
		}
	}

	private fun setupData() {

	}

	private fun observeLiveData() {


	}
}