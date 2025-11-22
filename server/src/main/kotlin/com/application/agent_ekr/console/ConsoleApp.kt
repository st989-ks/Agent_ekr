package com.application.agent_ekr.console

import com.application.agent_ekr.lmm.models.ChatMessage
import com.application.agent_ekr.lmm.models.Role
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.internal.writeJson

class ConsoleApp(){

    suspend fun start(){

        val message = ChatMessage(
            role = Role.ROLE_USER,
            content = "TODO()"
        )
        println(message)
        val jsonString = Json.encodeToString(message)
        println(jsonString)
//        print(Env.GIGACHAT_TOKEN)
    }

}