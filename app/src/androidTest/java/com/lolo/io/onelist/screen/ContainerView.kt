package com.lolo.io.onelist.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.lolo.io.onelist.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ContainerItemView: KScreen<ContainerItemView>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val listItems = KRecyclerView(
        builder = { withId(R.id.itemsRecyclerView) },
        itemTypeBuilder = { itemType(RecyclerView::NoteItem) }
    )

    class NoteItem(matcher: Matcher<View>) : KRecyclerItem<NoteItem>(matcher) {

        val text = KTextView(matcher) { withId(R.id.text_item_on_list) }
        val comment = KTextView(matcher) { withId(R.id.comment) }

    }
}