package com.library.telegramkotlinapi

import com.library.telegramkotlinapi.handler.AuthorizationResponseHandler
import com.library.telegramkotlinapi.handler.CheckCodeResponseHandler
import com.library.telegramkotlinapi.handler.GetCurrentUserResponseHandler
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class SimpleTelegramApi {

    private var client: Client? = null

    fun client(client: Client): SimpleTelegramApi {
        this.client = client
        return this
    }

    fun authWithPhone(phone: String, successHandle: ()->Unit, errorHandle: (Int)->Unit,
                      tooManyRequests: ()->Unit) {
        val phoneNumber = phone.replace(" ", "")
        client?.send(TdApi.SetAuthenticationPhoneNumber(phoneNumber, false, false),
            AuthorizationResponseHandler(successHandle, errorHandle, tooManyRequests)
        )
    }

    fun checkVerificationCode(code: String, successHandle: ()->Unit, errorHandle: (Int)->Unit) {
        client?.send(TdApi.CheckAuthenticationCode(code, null, null), CheckCodeResponseHandler(successHandle, errorHandle))
    }

    fun getUserInfo(successHandle: (TelegramUser) -> Unit, errorHandle: (Int) -> Unit) {
        client?.send(TdApi.GetMe(), GetCurrentUserResponseHandler(successHandle, errorHandle))
    }

}