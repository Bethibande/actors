package com.bethibande.actors.grpc

import com.bethibande.actors.proto.MessageServiceGrpcKt
import com.bethibande.actors.proto.Messages

class MessageServiceImpl: MessageServiceGrpcKt.MessageServiceCoroutineImplBase() {

    override suspend fun sendMessage(request: Messages.SendMessageRequest): Messages.MessageResponse {
        TODO("Not yet implemented")
    }
}