package com.application.agent_ekr.backend

import com.application.agent_ekr.Greeting
import com.application.agent_ekr.SERVER_PORT
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.slf4j.Logger

class BackendApp(
    val logger: Logger,
){
    suspend fun start(){
        embeddedServer(
            factory = Netty,
            port = SERVER_PORT,
            host = "0.0.0.0",
            module = {
                homeModule()
            }
        )
            .start(wait = true)
    }


    private fun Application.homeModule() {
        routing {
            get("/") {
                call.respondText("Ktor: ${Greeting().greet()}")
            }
        }
    }
}