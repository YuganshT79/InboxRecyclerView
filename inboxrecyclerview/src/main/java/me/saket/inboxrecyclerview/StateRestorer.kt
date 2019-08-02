package me.saket.inboxrecyclerview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

internal class StateRestorer(private val recyclerView: InboxRecyclerView) {

  private var itemToRestore: InboxRecyclerView.ExpandedItem = InboxRecyclerView.ExpandedItem.EMPTY

  internal fun save(outState: Parcelable?): Parcelable {
    return SavedState(outState, recyclerView.expandedItem)
  }

  internal fun restore(inState: Parcelable): Parcelable? {
    val savedState = inState as SavedState
    itemToRestore = savedState.expandedItem
    restoreIfPossible()
    return savedState.superState
  }

  internal fun restoreIfPossible() {
    val adapter = recyclerView.adapter
    val page = recyclerView.page

    if (itemToRestore.isEmpty().not() && page != null && adapter != null) {
      recyclerView.expandItem(itemToRestore.itemId, immediate = true)
    }
  }
}

@Parcelize
private data class SavedState(
    val superState: Parcelable?,
    val expandedItem: InboxRecyclerView.ExpandedItem
) : Parcelable
