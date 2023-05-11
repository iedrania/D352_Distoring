package com.iedrania.distoring.utils

import com.iedrania.distoring.data.model.Story

object DataDummy {
    fun generateDummyStory(): List<Story> {
        val storyList = ArrayList<Story>()
        for (i in 0..10) {
            val story = Story(
                "id-$i",
                "Name $i",
                "Lorem ipsum dolot sit amet.",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                0.toDouble(),
                0.toDouble()
            )
            storyList.add(story)
        }
        return storyList
    }
}