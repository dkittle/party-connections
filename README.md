# Party Connections

An app to generate D&amp;D party shared connections

## Walk-Thru

This app is a web app that uses the [llama3](https://huggingface.co/docs/transformers/main/en/model_doc/llama3) 8B model to generate shared connections between a party of Dungeons and Dragons player characters.

It uses the [Ollama](https://github.com/ollama/ollama) library to run the model.

The application uses server side rendering to generate the HTML pages.

The application uses [Tailwind CSS](https://tailwindcss.com/) for styling.

The application structure is a foray into Vertical Slice Architecture, where each capability (feature) is a separate package with one Kotlin source file each capability (create a party of characters, generate shared connections, etc.).

Within each source file will be the route for the capability along with the "service" and repository functions needed to facilitate the capability. Models used by the service functions are stored in a `models` package under the `capability` package. Entity helpers (with the MongoDB collection name and helper functions to convert Kotlin data classes to and from Mongo Documents) are store outside the `capabilities` package, in a top level `db` package.

The `plugins` package configures various aspects of the application including the HTTP Server, MongoDB connection, and the HTTP Client that interacts with Ollama.

The `web` package contains the `index` route and HTML elements along with a Styles CSS file that offers sets of Tailwind CSS classes for components.

TODO currently the connection to Ollama is done within an `Application` function in the plugins package and this needs to be moved elsewhere.

## Local Dev Setup

Add `127.0.0.1 host.docker.internal` to your host machine's `/etc/hosts` file.

Run `docker compose up` to create a single node MongoDB replica set and start the Ollama server.

Set the environment variable `LLM_URL=http://localhost:11434/api/`

## Run the app

The first time the app runs, Ollama will download the llama3 8B parameter model, so it'll take a while to start.

