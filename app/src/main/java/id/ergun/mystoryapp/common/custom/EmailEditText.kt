package id.ergun.mystoryapp.common.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import id.ergun.mystoryapp.R

/**
 * @author erikgunawan
 * Created 18/11/22 at 00.46
 */
class EmailEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    this@EmailEditText.error =
                        this@EmailEditText.context.getString(R.string.input_email_validation_error)
                }

            }
        })
    }
}