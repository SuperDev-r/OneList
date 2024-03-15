package com.lolo.io.onelist.screen

import androidx.test.espresso.matcher.ViewMatchers
import com.kaspersky.kaspresso.screens.KScreen
import com.lolo.io.onelist.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KTextView

object OneScreen : KScreen<OneScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val addList = KEditText { withId(R.id.addItemEditText) }
    val listName = KTextView { withId(R.id.textView) }
    val itemInList = KTextView { ViewMatchers.withId(R.id.text_item_on_list) }

    private val item = KTextView { withId(R.id.validate) }

}