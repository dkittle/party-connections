# Party Connections

An app to generate D&amp;D party shared connections

## Local Dev Setup

Add `127.0.0.1 host.docker.internal` to your host machine's `/etc/hosts` file.

Run `docker compose up` to create a single node MongoDB replica set.

Start Ollama in server mode with `ollama serve`

Set the environment variable `LLM_URL=http://localhost:11434/api/`

## Run the app

The first time the app runs, Ollama will download the llama3 80B parameter model so it'll take a while to start.

