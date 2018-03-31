package com.pandulapeter.campfire.feature.shared.widget

import android.content.Context
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.ViewToolbarTextInputBinding
import com.pandulapeter.campfire.util.animatedVisibilityEnd
import com.pandulapeter.campfire.util.hideKeyboard
import com.pandulapeter.campfire.util.showKeyboard

class ToolbarTextInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = DataBindingUtil.inflate<ViewToolbarTextInputBinding>(LayoutInflater.from(context), R.layout.view_toolbar_text_input, this, true).apply {
        textInput.run {
            hint = context.getString(R.string.library_search)
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            visibility = View.INVISIBLE
        }
    }

    val title = binding.title
    var isTextInputVisible = false
        set(value) {
            if (field != value) {
                field = value
                binding.title.animatedVisibilityEnd = !value
                binding.textInput.animatedVisibilityEnd = value
                if (value) {
                    showKeyboard(binding.textInput)
                } else {
                    hideKeyboard(binding.textInput)
                }
            }
        }
}