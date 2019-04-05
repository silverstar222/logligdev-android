package loglig.utility;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtility {
    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(InputMethodService.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(InputMethodService.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
