import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * https://gist.github.com/lopspower/6e20680305ddfcb11e1e
 */
object KeyboardUtils {
    fun hideKeyboard(activity: Activity) {
        val view =
            activity.findViewById<View>(R.id.content)
        if (view != null) {
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    fun addKeyboardVisibilityListener(
        rootLayout: View,
        onKeyboardVisibiltyListener: OnKeyboardVisibiltyListener
    ) {
        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootLayout.rootView.height
            // r.bottom is the position above soft keypad or device button.
// if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom
            val isVisible =
                keypadHeight > screenHeight * 0.15 // 0.15 ratio is perhaps enough to determine keypad height.
            onKeyboardVisibiltyListener.onVisibilityChange(isVisible)
        }
    }

    interface OnKeyboardVisibiltyListener {
        fun onVisibilityChange(isVisible: Boolean)
    }
}