package uz.abdullajon.videodownloaderapp.util

import android.net.Uri
import android.util.Log

fun getSize(size: Int): String {
    var s = ""
    val kb = (size / 1024).toDouble()
    val mb = kb / 1024
    val gb = kb / 1024
    val tb = kb / 1024
    if (size < 1024) {
        s = "$size Bytes"
    } else if (size >= 1024 && size < 1024 * 1024) {
        s = String.format("%.2f", kb) + " KB"
    } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
        s = String.format("%.2f", mb) + " MB"
    } else if (size >= 1024 * 1024 * 1024 && size < 1024 * 1024 * 1024 * 1024) {
        s = String.format("%.2f", gb) + " GB"
    } else if (size >= 1024 * 1024 * 1024 * 1024) {
        s = String.format("%.2f", tb) + " TB"
    }
    return s
}

fun getUriFromUrl(url: String): Uri = Uri.parse(url)



fun String.last4Element() =
    this.substring(this.lastIndex - 3, this.lastIndex + 1)


fun filterMP4(url: String): String {
    return if (url == "") {
        "EMPTY TEXT"
    } else {
        if (url.last4Element() == FILTER_MP4) {
            url
        } else {
            Log.d("AAAAA",url.last4Element())
            "Enter MP4 file"
        }
    }


}