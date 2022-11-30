package id.ergun.mystoryapp.common.util

import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat

/**
 * @author erikgunawan
 * Created 17/11/22 at 01.36
 */
class ActivityResultLauncher<Input, Result> private constructor(
    caller: ActivityResultCaller,
    contract: ActivityResultContract<Input, Result>
) {

    private var onActivityResult: OnActivityResult<Result>? = null

    private val launcher = caller.registerForActivityResult(contract) { result ->
        onActivityResult?.onActivityResult(result)
    }

    fun launch(
        intent: Input,
        onActivityResult: OnActivityResult<Result>?,
        options: ActivityOptionsCompat? = null
    ) {
        this.onActivityResult = onActivityResult
        launcher.launch(intent, options)
    }

    interface OnActivityResult<O> {
        fun onActivityResult(result: O)
    }

    companion object {
        fun register(caller: ActivityResultCaller) = register(
            caller, ActivityResultContracts.StartActivityForResult()
        )

        fun <LauncherIntent, LauncherResult> register(
            caller: ActivityResultCaller,
            contract: ActivityResultContract<LauncherIntent, LauncherResult>
        ) = ActivityResultLauncher(caller, contract)
    }
}