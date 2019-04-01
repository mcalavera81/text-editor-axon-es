package com.poc.command.domain

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateDocumentCommand(@TargetAggregateIdentifier val id: String, val name: String)
data class DeleteDocumentCommand(@TargetAggregateIdentifier val id: String)
data class AppendLineCommand(@TargetAggregateIdentifier val id: String, val line: String)
data class UpdateLineCommand(@TargetAggregateIdentifier val id: String, val number:Int, val line: String)