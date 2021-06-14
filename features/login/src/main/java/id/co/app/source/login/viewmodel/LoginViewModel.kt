package id.co.app.source.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.app.source.core.base.base.Dispatchers
import id.co.app.source.usersession.UserSession
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
	private val userSession: UserSession,
	private val dispatcher: Dispatchers
) : ViewModel() {


	fun login(userName: String, password: String, locationName: String) {
		viewModelScope.launch(dispatcher.io()) {

		}
	}
}