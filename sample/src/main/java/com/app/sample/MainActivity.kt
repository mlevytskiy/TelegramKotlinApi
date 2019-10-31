package com.app.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.library.telegramkotlinapi.SimpleTelegramApi
import com.library.telegramkotlinapi.SimpleTelegramApi.AuthWithPhoneResult.*
import com.library.telegramkotlinapi.TelegramUser
import com.library.telegramkotlinapi.handler.AuthorizationResponseHandler
import com.library.telegramkotlinapi.handler.CommonHandler
import kotlinx.coroutines.*
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.Client.ExceptionHandler

class MainActivity : AppCompatActivity() {

    private val supervisor = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + supervisor)

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
        startBgJob {
            val result = SimpleTelegramApi().client(client!!).authWithPhone(editText?.text.toString())
            when (result) {
                SUCCESS -> {
                    showToast("onClickSendPhone success")
                }
                ERROR -> {
                    showToast( "onClickSendPhone error")
                }
                ERROR_TOO_MANY_REQUESTS -> {
                    showToast("Too many requests") }
                }
            }
        }

    fun onClickSendCode(view: View) {
        startBgJob {
            val api = SimpleTelegramApi().client(client!!)
            val isSuccess = api.checkVerificationCode(codeEditText?.text.toString())
            var user: TelegramUser? = null
            if (isSuccess) {
                user = api.getUserInfo()
            }
            showToast(if (isSuccess && user!= null) "onClickSendCode success \n $user" else "onClickSendCode failed")
        }
    }

    private fun showToast(message: String) {
        runOnUiThread({Toast.makeText(this, message, Toast.LENGTH_LONG).show()})
    }

    fun startBgJob(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch(block = {
            block.invoke(this)
        })
    }


}
