package com.poc.command.event

import org.axonframework.modelling.command.TargetAggregateIdentifier


interface DocumentEvent{
    val id: String

    fun isCompensatable():Boolean
    fun isCompensation():Boolean
    fun buildCompensation():UndoEvent
}

data class DocumentCreatedEvent(override val id: String, val name: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent { throw UnsupportedOperationException()}
}

data class DocumentDeletedEvent(override val id: String):DocumentEvent{
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent { throw UnsupportedOperationException()}

}
data class AppendedLineEvent(override val id: String, val line: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoAppendedLineEvent(id)
}

data class UpdatedLineEvent(override val id: String, val number:Int, val line: String, val oldLine: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoUpdatedLineEvent(id, number, oldLine)
}

interface UndoEvent
data class UndoAppendedLineEvent(@TargetAggregateIdentifier override val id: String):UndoEvent,DocumentEvent {
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}

data class UndoUpdatedLineEvent(@TargetAggregateIdentifier override val id: String, val number:Int, val line: String):UndoEvent,DocumentEvent{
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}
