package com.amadiyawa.feature_base.domain.event

import com.amadiyawa.feature_base.domain.model.DomainEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST

/**
 * Domain event bus for cross-feature communication
 * Managed as a singleton by Koin DI container
 */
class DomainEventBus {
    private val _events = MutableSharedFlow<DomainEvent>(
        replay = 0,
        extraBufferCapacity = 64, // Buffer for multiple subscribers
        onBufferOverflow = DROP_OLDEST
    )

    val events: Flow<DomainEvent> = _events.asSharedFlow()

    suspend fun emit(event: DomainEvent) {
        _events.emit(event)
    }

    inline fun <reified T : DomainEvent> subscribe(): Flow<T> {
        return events.filter { it is T }.map { it as T }
    }
}