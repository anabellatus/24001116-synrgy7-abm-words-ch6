package com.anabell.words.ui.fragment.profile

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.anabell.words.data.datasource.local.AuthLocalDataSourceImpl
import com.anabell.words.data.datasource.local.dataStore
import com.anabell.words.data.datasource.remote.AuthRemoteDataSourceImpl
import com.anabell.words.data.repository.AuthRepositoryImpl
import com.anabell.words.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    ) : ViewModel() {

    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            context: Context,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, null) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle,
                ): T {
                    val authRepository: AuthRepository = AuthRepositoryImpl(
                        authLocalDataSource = AuthLocalDataSourceImpl(
                            dataStore = context.dataStore,
                        ),
                        authRemoteDataSource = AuthRemoteDataSourceImpl(),
                    )
                    return ProfileViewModel(
                        authRepository = authRepository
                    ) as T
                }
            }
    }

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> = _userEmail

    fun getProfileData() {
        viewModelScope.launch {
            try {
//                _userName.value = authRepository.loadUserName()
                _userEmail.value = authRepository.loadUserEmail()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.clearToken()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }

    }
}