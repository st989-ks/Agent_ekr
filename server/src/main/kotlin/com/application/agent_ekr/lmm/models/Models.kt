package com.application.agent_ekr.lmm.models

/**
 * Data classes representing the structure of the `/models` endpoint response from GigaChat API.
 *
 * This corresponds to the OpenAPI specification defined in `.aditional/gigachat_api.yml`
 */

data class ModelsResponse(
    val data: List<Model>,
    val object: String // Type of entity returned ("list").
)

data class Model(
    val id: String,       // Unique identifier of the model.
    val object: String,   // Type of entity returned ("model").
    val ownedBy: String,  // Owner of the model.
    val type: String      // Type of model (e.g., "chat", "aicheck", "embedder").
)
