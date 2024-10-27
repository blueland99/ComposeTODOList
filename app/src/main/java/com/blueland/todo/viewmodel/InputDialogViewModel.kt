package com.blueland.todo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputDialogViewModel @Inject constructor() : ViewModel() {
    private val _isDialogVisible = mutableStateOf(false)
    val isDialogVisible: State<Boolean> = _isDialogVisible

    private var onConfirmAction: (password: String) -> Unit = {}
    private var onDismissAction: () -> Unit = {}

    private val _dialogTitle = mutableStateOf<String?>(null)
    val dialogTitle: State<String?> = _dialogTitle

    private val _dialogContent = mutableStateOf<String?>(null)
    val dialogContent: State<String?> = _dialogContent

    private val _dialogHint = mutableStateOf<String?>(null)
    val dialogHint: State<String?> = _dialogHint

    fun showDialog(
        title: String? = null,
        content: String? = null,
        hint: String? = null,
        onConfirm: (String) -> Unit,
        onDismiss: () -> Unit = {}
    ) {
        onConfirmAction = onConfirm
        onDismissAction = onDismiss
        _dialogTitle.value = title
        _dialogContent.value = content
        _dialogHint.value = hint
        _isDialogVisible.value = true
    }

    fun hideDialog() {
        _isDialogVisible.value = false
    }

    fun onConfirm(password: String) {
        onConfirmAction(password)
        hideDialog()
    }

    fun onDismiss() {
        onDismissAction()
        hideDialog()
    }
}
