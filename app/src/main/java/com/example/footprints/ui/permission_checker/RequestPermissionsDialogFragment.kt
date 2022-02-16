package com.example.footprints.ui.permission_checker

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RequestPermissionsDialogFragment(
    private val notification: String,
    private val listener: NotificationDialogListener
) : DialogFragment() {

    interface NotificationDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: AlertDialog = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("使用上の注意")
                .setMessage(notification)
                .setPositiveButton("はい", getOnDialogPositiveClickListener())
                .setNegativeButton("いいえ", getOnDialogNegativeClickListener())

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun getOnDialogPositiveClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { p0, p1 ->
            listener.onDialogPositiveClick(this)
        }
    }

    private fun getOnDialogNegativeClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { p0, p1 ->
            listener.onDialogNegativeClick(this)
        }
    }
}