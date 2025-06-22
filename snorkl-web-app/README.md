# ✅  Test & Build
Run locally:

```bash
npm run dev
```

Build for Android or Production:

```bash
npm run build
```
You’ll get a dist/ folder with static HTML, JS, and CSS.

# Render the compiled pages in android

## Build the web-app to produce compiled html, js and css files
1. Copy the content of `dist` folder

## Put the compiled the web-app into android directory
1. Put it in /Project/snorkl/src/assets/web2/assets

## Run the web-app from android src

```kotlin
get("/assets/{path...}") {
    val path = call.parameters.getAll("path")?.joinToString("/") ?: return@get call.respond(HttpStatusCode.BadRequest)
    val assetPath = "web2/assets/$path"

    val result = getTextFromAsset(context.assets, assetPath)
    result.onSuccess {
        val contentType = when {
            path.endsWith(".js") -> ContentType.Application.JavaScript
            path.endsWith(".css") -> ContentType.Text.CSS
            path.endsWith(".svg") -> ContentType.Image.SVG
            path.endsWith(".map") -> ContentType.Application.Json
            else -> ContentType.Application.OctetStream
        }
        call.respondText(it, contentType)
    }.onFailure {
        call.respondText("Not Found", ContentType.Text.Plain, HttpStatusCode.NotFound)
    }
}
```