package com.example.todo2

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import com.example.todo2.activities.MainActivity
import org.jetbrains.anko.doAsync

class DialogDeletion {
    fun createDialog(activity: MainActivity, adapter: CustomAdapter, pos: Int, data: MutableList<Task>) : AlertDialog? {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.apply {
            setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        data.removeAt(pos)
                        adapter.notifyDataSetChanged()
                    })
            setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        adapter.notifyItemChanged(pos)
                    })
        }

        builder?.setMessage(R.string.dialog_delete)?.setTitle(R.string.mes_delete)

        return builder?.create()
    }
}