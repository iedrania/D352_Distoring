package com.iedrania.distoring

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val name: String,
    val description: String,
    val photoURL: String,
    val createdAt: String,
) : Parcelable