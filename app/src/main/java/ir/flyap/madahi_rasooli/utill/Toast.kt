package ir.flyap.madahi_rasooli.utill

import android.content.Context
import android.widget.Toast


fun Context.showToast(textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, textId, duration).show()
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}
