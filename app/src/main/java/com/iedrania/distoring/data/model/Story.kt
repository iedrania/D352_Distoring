package com.iedrania.distoring.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
) : Parcelable