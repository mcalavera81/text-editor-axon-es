package com.poc.command.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier


interface Command
data class CreateDocumentCommand(@TargetAggregateIdentifier val id: String, val name: String):Command
data class DeleteDocumentCommand(@TargetAggregateIdentifier val id: String):Command
data class AppendLineCommand(@TargetAggregateIdentifier val id: String, val line: String):Command
data class UpdateLineCommand(@TargetAggregateIdentifier val id: String, val number:Int, val line: String):Command
data class UndoCommand(@TargetAggregateIdentifier val id: String):Command

