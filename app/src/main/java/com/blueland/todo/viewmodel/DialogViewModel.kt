package com.blueland.todo.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.blueland.todo.enums.DialogType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor() : ViewModel() {
    private val _isDialogVisible = mutableStateOf(false)
    val isDialogVisible: State<Boolean> = _isDialogVisible

    private var onConfirmAction: () -> Unit = {}
    private var onDismissAction: () -> Unit = {}

    private val _dialogTitle = mutableStateOf<String?>(null)
    val dialogTitle: State<String?> = _dialogTitle

    private val _dialogContent = mutableStateOf<String?>(null)
    val dialogContent: State<String?> = _dialogContent

    private val _dialogConfirm = mutableStateOf<String?>(null)
    val dialogConfirm: State<String?> = _dialogConfirm

    private val _dialogCancel = mutableStateOf<String?>(null)
    val dialogCancel: State<String?> = _dialogCancel

    private val _currentDialogType = mutableStateOf(DialogType.CONFIRM)
    val currentDialogType: State<DialogType> = _currentDialogType


    fun showDialog(
        dialogType: DialogType,
        title: String? = null,
        content: String? = null,
        confirm: String? = null,
        cancel: String? = null,
        onConfirm: () -> Unit,
        onDismiss: () -> Unit = {}
    ) {
        onConfirmAction = onConfirm
        onDismissAction = onDismiss
        _currentDialogType.value = dialogType
        _dialogTitle.value = title
        _dialogContent.value = content
        _dialogConfirm.value = confirm
        _dialogCancel.value = cancel
        _isDialogVisible.value = true
    }

    fun hideDialog() {
        _isDialogVisible.value = false
    }

    fun onConfirm() {
        onConfirmAction()
        hideDialog()
    }

    fun onDismiss() {
        if (currentDialogType.value == DialogType.CONFIRM) {
            onDismissAction()
            hideDialog()
        }
    }
}
