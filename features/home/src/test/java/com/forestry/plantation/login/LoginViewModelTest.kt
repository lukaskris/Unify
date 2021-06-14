package com.forestry.plantation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.co.app.source.core.domain.entities.login.Profile
import id.co.app.source.core.domain.entities.login.User
import com.forestry.plantation.core.domain.entities.master.Region
import com.forestry.plantation.core.extension.isFailure
import com.forestry.plantation.login.viewmodel.LoginViewModel
import com.forestry.plantation.usersession.UserSession
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException
import kotlin.time.ExperimentalTime


/**
 * Created by Lukas Kristianto on 4/28/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@ExperimentalCoroutinesApi
@ExperimentalTime
class LoginViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	private val mockLoginUseCase = mockk<LoginUseCase>()
	private val mockGetRegionUseCase = mockk<GetRegionUseCase>()
	private val mockUserSession = mockk<UserSession>(relaxed = true)
	private val observer: Observer<Result<List<Region>>> = mockk(relaxed = true)
	private val viewModel: LoginViewModel = LoginViewModel(
		mockGetRegionUseCase,
		mockLoginUseCase,
		mockUserSession,
		TestDispatchers()
	)

	@Before
	fun setup() {
		viewModel.region.observeForever(observer)
	}

	@Test
	fun `Test get region success`() {
		runBlocking {
			val listData = listOf(Region("1", "HQ"))
			val loading = Result.Loading
			val success = Result.Success(listData)
			every { mockGetRegionUseCase.execute() } returns flow {
				emit(listData)
			}
			viewModel.getRegion()
			verifyOrder {
				observer.onChanged(loading)
				observer.onChanged(success)
			}
		}
	}

	@Test
	fun `Test get region error`() {
		runBlocking {
			val slots = mutableListOf<Result<List<Region>>>()
			every { mockGetRegionUseCase.execute() } returns flow {
				throw TimeoutException()
			}
			viewModel.getRegion()
			verify {
				observer.onChanged(capture(slots))
			}
			Truth.assertThat(slots[0]).isSameInstanceAs(Result.Loading)
			Truth.assertThat(slots[1]).isInstanceOf(Result.Failure::class.java)
		}
	}

	@Test
	fun `Test login success`() {
		runBlockingTest {
			val listData = listOf(Region("1", "HQ"))
			val user = User(
				Profile(
					"1", "", "", "", "", "", "", "", "", "", ""
				),
				"token"
			)
			every { mockLoginUseCase.execute(any(), any(), any()) } returns flow {
				emit(user)
			}
			every { mockGetRegionUseCase.execute() } returns flow {
				emit(listData)
			}
			viewModel.getRegion()
			viewModel.login("budi", "123", "HQ")
			assert((viewModel.loginState.value as? Result.Success<User>)?.value?.token?.isNotEmpty() == true)
		}
	}

	@Test
	fun `Test login error`() {
		runBlockingTest {
			val listData = listOf(Region("1", "HQ"))
			every { mockGetRegionUseCase.execute() } returns flow {
				emit(listData)
			}
			every { mockLoginUseCase.execute(any(), any(), any()) } returns flow {
				throw Exception()
			}
			viewModel.getRegion()
			viewModel.login("budi", "123", "HQ")

			assert(viewModel.loginState.first()?.isFailure == true)
		}
	}
}