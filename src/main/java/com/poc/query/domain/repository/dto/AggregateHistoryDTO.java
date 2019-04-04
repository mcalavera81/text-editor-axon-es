package com.poc.query.domain.repository.dto;

import com.poc.command.event.DocumentEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@AllArgsConstructor
@Getter
@ToString
public class AggregateHistoryDTO
{
	private String name;

	private DocumentEvent event;  // All my events extends DocumentEvent

	private long sequenceNumber;

	private Instant timestamp;
}