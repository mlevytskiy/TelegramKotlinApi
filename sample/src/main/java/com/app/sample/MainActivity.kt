package com.app.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.library.telegramkotlinapi.SimpleTelegramApi
import com.library.telegramkotlinapi.handler.AuthorizationResponseHandler
import com.library.telegramkotlinapi.handler.CommonHandler
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.Client.ExceptionHandler

class MainActivity : AppCompatActivity() {

    private var client: Client? = null
    private var editText: EditText? = null
    private var codeEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.edit_text)
        codeEditText = findViewById(R.id.code_edit_text)
    }

    fun onClickInstall(view: View) {
        val handler = CommonHandler(this)
        client = Client.create(handler, object : ExceptionHandler {
            override fun onException(e: Throwable?) {
                Log.i("testr", "onException " + e)
            }

        }, object : ExceptionHandler {
            override fun onException(e: Throwable?) {
                Log.i("testr", "onException " + e)
            }

        })
        handler.client = client
    }

    /**
     * variant1:
     * TdApi.UpdateAuthorizationState.CONSTRUCTOR state=AuthorizationStateWaitCode {
    isRegistered = true
    termsOfService = TermsOfService {
    text = FormattedText {
    text = "Создавая аккаунт в Telegram, Вы подтверждаете, что не станете:

    - использовать его для спама или мошенничества,
    - насаждать насилие через публичные каналы, группы или ботов,
    - распространять порнографические материалы через публичные каналы, группы или ботов.

    Мы оставляем за собой право обновлять это соглашение в дальнейшем."
    entities = Array[0] {
    }
    }
    minUserAge = 0
    showPopup = false
    }
    codeInfo = AuthenticationCodeInfo {
    phoneNumber = "+380637674444"
    type = AuthenticationCodeTypeSms {
    length = 5
    }
    nextType = null
    timeout = 0
    }
    }
     * variant2:
    TdApi.UpdateAuthorizationState.CONSTRUCTOR state=AuthorizationStateWaitCode {
    isRegistered = true
    termsOfService = TermsOfService {
    text = FormattedText {
    text = "Создавая аккаунт в Telegram, Вы подтверждаете, что не станете:

    - использовать его для спама или мошенничества,
    - насаждать насилие через публичные каналы, группы или ботов,
    - распространять порнографические материалы через публичные каналы, группы или ботов.

    Мы оставляем за собой право обновлять это соглашение в дальнейшем."
    entities = Array[0] {
    }
    }
    minUserAge = 0
    showPopup = false
    }
    codeInfo = AuthenticationCodeInfo {
    phoneNumber = "+380637674444"
    type = AuthenticationCodeTypeTelegramMessage {
    length = 5
    }
    nextType = AuthenticationCodeTypeSms {
    length = 0
    }
    timeout = 0
    }
    }
     */

    fun onClickSendPhone(view: View) {
        SimpleTelegramApi().client(client!!)
            .authWithPhone(editText?.text.toString(),
                { showToast("onClickSendPhone success") },
                { code->showToast( "onClickSendPhone failed code=$code") },
                { showToast("Too many requests") } )
    }

    fun onClickSendCode(view: View) {
        SimpleTelegramApi().client(client!!)
            .checkVerificationCode(codeEditText?.text.toString(),
                { showToast("onClickSendCode success") },
                { code->showToast( "onClickSendCode failed code=$code") })
    }

    private fun showToast(message: String) {
        runOnUiThread({Toast.makeText(this, message, Toast.LENGTH_LONG).show()})
    }


}
