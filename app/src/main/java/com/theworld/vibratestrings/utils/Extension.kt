package com.theworld.vibratestrings.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun <A : Activity> Activity.openActivity(activity: Class<A>) {
    Intent(this, activity).also {
        startActivity(it)
    }
}

/*------------------------------------- Display Snackbar ------------------------------*/

fun View.snackbar(message: String?) {
    val snackbar = Snackbar.make(this, message ?: "", Snackbar.LENGTH_LONG)
    snackbar.show()
}

/*------------------------------------- Display Toast ------------------------------*/

fun Context.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/*------------------------------------- IS Valid URL ------------------------------*/

fun String?.isValidUrl(): Boolean {
    val regex = ("((http|https)://)(www.)?"
            + "[a-zA-Z0-9@:%._\\+~#?&//=]"
            + "{2,256}\\.[a-z]"
            + "{2,6}\\b([-a-zA-Z0-9@:%"
            + "._\\+~#?&//=]*)")

    if (this == null) {
        return false
    }

    val p = Pattern.compile(regex)

    val m: Matcher = p.matcher(this)

    return m.matches()
}

/*------------------------------------- Listen Text Change ------------------------------*/


fun EditText.afterTextChange(afterTextChanged: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {


        }

        override fun onTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}


/*------------------------------------- Enable TextView ------------------------------*/

fun TextView.enableTextView(enabled: Boolean) {
    this.isEnabled = enabled

    if (!enabled) {
        this.alpha = 0.5f
    } else {
        this.alpha = 1f
    }

}
/*------------------------------------- Bitmap to String ------------------------------*/

fun Bitmap.getStringImage(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

/*------------------------------------- String to Bitmap ------------------------------*/

fun String.decodeStringToImage(): Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}


/*------------------------------------- Is Email Valid ------------------------------*/

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}
/*------------------------------------- Edit Text to String ------------------------------*/

fun TextInputLayout.normalText() = this.editText?.text.toString().trim()
fun EditText.normalText() = this.text.toString().trim()
fun TextInputLayout.upperCaseText() =
    this.editText?.text.toString().toUpperCase(Locale.getDefault()).trim()


/*------------------------------------- Edit Text Validation ------------------------------*/

fun EditText.customValidation(validation: CustomValidation): Boolean {
    val text = this.normalText()

    if (text.isEmpty()) {
        this.error = "Field can't be empty"
        return false
    }

    if (validation.isEmail) {
        this.error = if (text.isEmailValid()) null else "Invalid Email"
        return text.isEmailValid()
    }

    if (validation.isLengthRequired) {
        val length = validation.length
        this.error =
            if (text.length == length) null else "Field should have $length digits/characters"
        return text.length == length
    }


    this.error = null
    return true
}


fun TextInputLayout.customValidation(validation: CustomValidation): Boolean {
    val text = this.normalText()

    if (text.isEmpty()) {
        this.error = "Field can't be empty"
        return false
    }

    if (validation.isEmail) {
        this.error = if (text.isEmailValid()) null else "Invalid Email"
        return text.isEmailValid()
    }

    if (validation.isLengthRequired) {
        val length = validation.length
        this.error =
            if (text.length == length) null else "Field should have $length digits/characters"
        return text.length == length
    }


    this.error = null
    return true
}

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}


fun Context.getCurrentSound(): Uri {

    var ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_RINGTONE
    )
    if (ringtoneUri == null) {
        // if ringtoneUri is null get Default Ringtone
        ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    }
    return ringtoneUri
}


fun Context.getOldRingtoneUri(): Uri {
    val sharedPrefManager = SharedPrefManager(this)
    val ringtoneString = sharedPrefManager.getString("old_ringtone_uri")

    return Uri.parse(ringtoneString)
}


fun Context.modifyRingtone(tuneUri: Uri) {

    RingtoneManager.setActualDefaultRingtoneUri(
        this,
        RingtoneManager.TYPE_RINGTONE,
        tuneUri
    )

}


inline fun Fragment.confirmDialog(
    title: String = "Confirmation",
    message: String = "Do you really want to delete?",
    positiveButton: String = "Yes",
    negativeButton: String = "No",
    displayNegativeButton: Boolean = true,
    crossinline invoke: () -> Unit,
) {
    val dialog = AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ ->

            invoke.invoke()

            dialog.dismiss()
        }

    if (displayNegativeButton) {
        dialog.setNegativeButton(negativeButton, null)
    }

    dialog.create().show()
}

inline fun Activity.confirmDialog(
    title: String = "Confirmation",
    message: String = "Do you really want to delete?",
    positiveButton: String = "Yes",
    negativeButton: String = "No",
    displayNegativeButton: Boolean = true,
    crossinline invoke: () -> Unit,
) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ ->

            invoke.invoke()

            dialog.dismiss()
        }

    if (displayNegativeButton) {
        dialog.setNegativeButton(negativeButton, null)
    }

    dialog.create().show()
}