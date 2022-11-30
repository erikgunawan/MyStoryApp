package id.ergun.mystoryapp.common.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import id.ergun.mystoryapp.R

/**
 * @author erikgunawan
 * Created 18/11/22 at 01.07
 */
class NameEditText : AppCompatEditText {

    private var errorBackground: Drawable? = null
    private var defaultBackground: Drawable? = null
    private var errorInput: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        defaultBackground = ContextCompat.getDrawable(context, R.drawable.bg_input_default)
        errorBackground = ContextCompat.getDrawable(context, R.drawable.bg_input_error)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val isInvalid = s != null && s.isEmpty()
                if (isInvalid) {
                    this@NameEditText.error =
                        this@NameEditText.context.getString(R.string.input_name_empty_error)
                }
                errorInput = isInvalid
            }
        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background =
            if (errorInput) errorBackground
            else defaultBackground
    }
}