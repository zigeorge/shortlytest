package test.geo.shortly.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.TextView
import test.geo.shortly.R

class LoadingView {

    private lateinit var dialog: Dialog

    private fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    fun show() {
        try {
            dialog.show()
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }

    fun hide() {
        try {
            dialog.dismiss()
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }

    companion object {
        fun createLoading(context: Context): LoadingView {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.loading_view)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color .TRANSPARENT))
            dialog.setCancelable(false)
            val loadingView = LoadingView()
            loadingView.setDialog(dialog)
            return loadingView
        }
    }

}