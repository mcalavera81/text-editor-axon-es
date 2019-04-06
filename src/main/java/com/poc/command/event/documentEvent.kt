package com.poc.command.event

import org.axonframework.modelling.command.TargetAggregateIdentifier


interface DocumentEvent{
    val id: String

    fun isCompensatable():Boolean
    fun isCompensation():Boolean
    fun buildCompensation():UndoEvent
}

data class DocumentCreatedEvent(override val id: String, val docName: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent { throw UnsupportedOperationException()}
}

data class DocumentDeletedEvent(override val id: String):DocumentEvent{
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent { throw UnsupportedOperationException()}

}
data class AppendedLineEvent(override val id: String, val appendedText: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoAppendedLineEvent(id)
}

data class UpdatedLineEvent(override val id: String, val lineNumber:Int, val text: String, val oldText: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoUpdatedLineEvent(id, lineNumber, oldText)
}

data class InsertedLineEvent(override val id: String, val lineNumber:Int, val insertedText: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoInsertedLineEvent(id, lineNumber)
}

data class RemovedLineEvent(override val id: String, val lineNumber:Int, val removedText: String):DocumentEvent {
    override fun isCompensation(): Boolean = false
    override fun isCompensatable(): Boolean = true
    override fun buildCompensation(): UndoEvent = UndoRemovedLineEvent(id, lineNumber, removedText)
}

interface UndoEvent
data class UndoAppendedLineEvent(@TargetAggregateIdentifier override val id: String):UndoEvent,DocumentEvent {
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}

data class UndoUpdatedLineEvent(@TargetAggregateIdentifier override val id: String, val lineNumber:Int, val text: String):UndoEvent,DocumentEvent{
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}

data class UndoInsertedLineEvent(@TargetAggregateIdentifier override val id: String, val lineNumber:Int):UndoEvent,DocumentEvent{
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}

data class UndoRemovedLineEvent(@TargetAggregateIdentifier override val id: String, val lineNumber:Int, val text:String):UndoEvent,DocumentEvent{
    override fun isCompensation(): Boolean = true
    override fun isCompensatable(): Boolean = false
    override fun buildCompensation(): UndoEvent = throw UnsupportedOperationException()
}
