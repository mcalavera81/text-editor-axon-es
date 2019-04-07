#!/usr/bin/env bash

server=http://localhost:8080
if ! [[ -x "$(command -v jq)" ]]; then
  echo 'Error: jq is not installed.' >&2
  exit 1
fi

docId=$(curl -s -d '{"name":"pepe"}' -H "Content-Type: application/json" -X POST $server/document | jq -r '.id')

{
curl -d '{"text": "First line"}' -H "Content-Type: application/json" -X POST $server/document/$docId/append
curl -d '{"text": "Second line"}' -H "Content-Type: application/json" -X POST $server/document/$docId/append
curl -d '{"text": "First line modified","lineNumber":1 }' -H "Content-Type: application/json" -X POST $server/document/$docId/update
curl -d '{"text": "Inserted at 1","lineNumber":1 }' -H "Content-Type: application/json" -X POST $server/document/$docId/insert
curl -d '{"text": "Inserted at 3","lineNumber":3 }' -H "Content-Type: application/json" -X POST $server/document/$docId/insert
curl -d '{"lineNumber":2 }' -H "Content-Type: application/json" -X POST $server/document/$docId/remove
} >/dev/null 2>&1

curl  -s $server/document/$docId/text | jq -r '.text'

curl  -X POST $server/document/$docId/undo
printf "\n\nundo remove...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'
printf "************************"

curl  -X POST $server/document/$docId/undo
printf "\n\nundo insert...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'
printf "************************"

curl  -X POST $server/document/$docId/undo
printf "\n\nundo insert...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'
printf "************************"

curl  -X POST $server/document/$docId/undo
printf "\n\nundo update...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'
printf "************************"

curl  -X POST $server/document/$docId/undo
printf "\n\nundo append...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'
printf "************************"

curl  -X POST $server/document/$docId/undo
printf "\n\nundo append...\n"
printf "************************\n"
curl  -s $server/document/$docId/text | jq -r '.text'



