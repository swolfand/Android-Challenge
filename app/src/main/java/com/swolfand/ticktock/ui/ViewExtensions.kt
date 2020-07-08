package com.swolfand.ticktock.ui

import android.view.View
import android.widget.TextView

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun TextView.str(): String = this.text.toString()

fun TextView.toInt(): Int = Integer.parseInt(this.str())