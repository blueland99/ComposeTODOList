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

    fun showDialog(
        onConfirm: (String) -> Unit,
        onDismiss: () -> Unit = {}
    ) {
        onConfirmAction = onConfirm
        onDismissAction = onDismiss
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
