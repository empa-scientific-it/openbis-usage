package ch.empa.usagecollector

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

enum class EventType {
    NEW_OBJECT,
    NEW_SPACE,
    NEW_DATASET,
    NEW_COLLECTION,
    NEW_PROJECT,
    NEW_USER
}

@Serializable
sealed class InstanceEvent(val eventCode: EventType) {
    abstract val date: LocalDateTime
    abstract val space: String?
    abstract val instance: String?

}

@Serializable
data class NewObjectEvent(override val date: LocalDateTime, override val space: String?, val collection: String?,
                          override val instance: String?,
) :
    InstanceEvent(EventType.NEW_OBJECT)

@Serializable
data class NewProjectEvent(
    override val date: LocalDateTime, override val space: String?, override val instance: String?,
) : InstanceEvent(EventType.NEW_PROJECT)

@Serializable
data class NewDatasetEvent(
    override val date: LocalDateTime, override val space: String?, val size: Long, override val instance: String?,
) : InstanceEvent(EventType.NEW_DATASET)

@Serializable
data class NewCollectionEvent(
    override val date: LocalDateTime, override val space: String?, override val instance: String?,
) : InstanceEvent(EventType.NEW_SPACE)

@Serializable
data class NewPersonEvent(
    override val date: LocalDateTime, override val space: String?, override val instance: String?,
) : InstanceEvent(EventType.NEW_USER)
