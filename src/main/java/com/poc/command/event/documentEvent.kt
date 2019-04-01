package com.poc.command.event

data class DocumentCreatedEvent(val id: String, val name: String)
data class DocumentDeletedEvent(val id: String)
data class AppendedLineEvent(val id: String, val line: String)
data class UpdatedLineEvent(val id: String, val number:Int, val line: String)