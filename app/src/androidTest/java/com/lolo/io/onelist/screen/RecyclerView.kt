package com.lolo.io.onelist.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.lolo.io.onelist.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object RecyclerView : KScreen<RecyclerView>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val lists = KRecyclerView(
        builder = { withId(R.id.listsRecyclerView) },
        itemTypeBuilder = { itemType(RecyclerView::NoteItem) }
    )

    class NoteItem(matcher: Matcher<View>) : KRecyclerItem<NoteItem>(matcher) {

        val listName = KTextView(matcher) { withId(R.id.textView) }

    }
}
