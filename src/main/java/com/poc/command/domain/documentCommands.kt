package com.poc.command.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier


interface Command
data class CreateDocumentCommand(@TargetAggregateIdentifier val id: String, val docName: String):Command
data class DeleteDocumentCommand(@TargetAggregateIdentifier val id: String):Command
data class AppendLineCommand(@TargetAggregateIdentifier val id: String, val lineNumber: String):Command
data class UpdateLineCommand(@TargetAggregateIdentifier val id: String, val lineNumber:Int, val text: String):Command
data class InsertLineCommand(@TargetAggregateIdentifier val id: String, val lineNumber:Int, val text: String):Command
data class RemoveLineCommand(@TargetAggregateIdentifier val id: String, val lineNumber:Int):Command
data class UndoCommand(@TargetAggregateIdentifier val id: String):Command

