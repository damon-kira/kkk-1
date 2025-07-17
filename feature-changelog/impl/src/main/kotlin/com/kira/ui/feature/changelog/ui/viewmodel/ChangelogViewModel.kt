

package com.kira.ui.feature.changelog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.feature.changelog.domain.model.ReleaseModel
import com.kira.ui.feature.changelog.domain.repository.ChangelogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChangelogViewModel @Inject constructor(
    private val changelogRepository: ChangelogRepository,
) : ViewModel() {

    private val _changelogState = MutableStateFlow<List<ReleaseModel>>(emptyList())
    val changelogState: StateFlow<List<ReleaseModel>> = _changelogState.asStateFlow()

    private val _viewEvent = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvent: Flow<ViewEvent> = _viewEvent.receiveAsFlow()

    init {
        fetchChangeLog()
    }

    private fun fetchChangeLog() {
        viewModelScope.launch {
            try {
                _changelogState.value = changelogRepository.loadChangelog()
            } catch (e: Exception) {
                Timber.e(e, e.message)
                _viewEvent.send(ViewEvent.Toast(e.message.orEmpty()))
            }
        }
    }
}