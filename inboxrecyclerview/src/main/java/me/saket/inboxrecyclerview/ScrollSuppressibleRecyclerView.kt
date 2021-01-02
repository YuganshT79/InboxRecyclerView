package me.saket.inboxrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Freezing layout using [setLayoutFrozen] isn't sufficient for blocking programmatic scrolls
 * i.e., scrolls not initiated by the user. These scrolls are either requested by the app or
 * by RV when a child View requests focus.
 * */
abstract class ScrollSuppressibleRecyclerView(
    context: Context,
    attrs: AttributeSet?
) : RecyclerView(context, attrs) {

  override fun attachLayoutAnimationParameters(
      child: View?, params: ViewGroup.LayoutParams,
      index: Int, count: Int
  ) {
    val layoutManager = layoutManager
    if (adapter != null && layoutManager is GridLayoutManager) {
      var animationParams = params.layoutAnimationParameters
          as? GridLayoutAnimationController.AnimationParameters
      if (animationParams == null) {
        // If there are no animation parameters, create new once and attach them to
        // the LayoutParams.
        animationParams = GridLayoutAnimationController.AnimationParameters()
        params.layoutAnimationParameters = animationParams
      }

      // Next we are updating the parameters

      // Set the number of items in the RecyclerView and the index of this item
      animationParams.count = count
      animationParams.index = index

      // Calculate the number of columns and rows in the grid
      val columns = layoutManager.spanCount
      animationParams.columnsCount = columns
      animationParams.rowsCount = count / columns

      // Calculate the column/row position in the grid
      val invertedIndex = count - 1 - index
      animationParams.column = columns - 1 - invertedIndex % columns
      animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
    } else {
      // Proceed as normal if using another type of LayoutManager
      super.attachLayoutAnimationParameters(child, params, index, count)
    }
  }

  abstract fun canScrollProgrammatically(): Boolean

  override fun scrollToPosition(position: Int) {
    if (canScrollProgrammatically()) {
      super.scrollToPosition(position)
    }
  }

  override fun smoothScrollToPosition(position: Int) {
    if (canScrollProgrammatically()) {
      super.smoothScrollToPosition(position)
    }
  }

  override fun smoothScrollBy(dx: Int, dy: Int) {
    if (canScrollProgrammatically()) {
      super.smoothScrollBy(dx, dy)
    }
  }

  override fun scrollTo(x: Int, y: Int) {
    if (canScrollProgrammatically()) {
      super.scrollTo(x, y)
    }
  }

  override fun scrollBy(x: Int, y: Int) {
    if (canScrollProgrammatically()) {
      super.scrollBy(x, y)
    }
  }
}
