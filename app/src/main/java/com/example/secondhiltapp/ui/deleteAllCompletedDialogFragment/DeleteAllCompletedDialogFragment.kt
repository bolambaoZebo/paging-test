package com.example.secondhiltapp.ui.deleteAllCompletedDialogFragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.secondhiltapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedDialogFragment : DialogFragment(){

    private val viewModel: DeleteAllViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.confirm_deletion))
            .setMessage(resources.getString(R.string.deletion_body))
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.onConfirmClick()
            }
            .create()

}