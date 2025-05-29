package rahul.lohra.networkmonitor.presentation.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rahul.lohra.networkmonitor.core.TimeUtil
import rahul.lohra.networkmonitor.data.local.entities.NetworkType
import rahul.lohra.networkmonitor.presentation.ui.NetworkMonitorColorDeepNaviBlue

@Composable
fun RestApiDetailsUi(restApiDetails: RestApiDetails) {
    Column(Modifier.padding(horizontal = 8.dp)) {
        Row {
            TitleText("Url:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.requestUrl)
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Method:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.method)
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Status code:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.responseCode.toString())
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Timestamp:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(TimeUtil.formatTimestamp(restApiDetails.timestamp))
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Headers:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(restApiDetails.requestHeaders.toString())
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Response Size:")
            Spacer(Modifier.width(4.dp))
            DescriptionText("${restApiDetails.body.length / 1024} kb")
        }
        Column(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Response:")
            Spacer(Modifier.height(4.dp))
            DescriptionText(restApiDetails.body)
        }
    }
}

@Composable
fun WsDetailsUi(wsDetails: WsDetails) {
    Column(Modifier.padding(horizontal = 8.dp)) {
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Url:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(wsDetails.requestUrl)
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Event:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(wsDetails.eventType)
        }

        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Direction:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(wsDetails.direction)
        }

        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Timestamp:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(TimeUtil.formatTimestamp(wsDetails.timestamp))
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Connection Id:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(wsDetails.connectionId)
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Response Size:")
            Spacer(Modifier.width(4.dp))
            DescriptionText("${wsDetails.messageSize.toLong() / 1024} kb")
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Message Type:")
            Spacer(Modifier.width(4.dp))
            DescriptionText(wsDetails.messageType)
        }

        Column(modifier = Modifier.padding(top = 4.dp)) {
            TitleText("Response:")
            Spacer(Modifier.height(4.dp))
            DescriptionText(wsDetails.body)
        }
    }
}

@Preview
@Composable
fun PreviewRestRequestDetailsUi() {
    val restApiDetails = RestApiDetails(
        id = "8a6afe0e-9d53-4a8d-a5c3-f5b06d47e186",
        networkType = NetworkType.REST.title,
        timestamp = 1748493681789,
        requestUrl = "https://echo.websocket.events/",
        method = "GET",
        requestHeaders = emptyMap(),
        responseCode = 200,
        responseHeaders = parseJsonWithGson(
            """
            {"access-control-allow-origin":["*"],"content-type":["application/json; charset=UTF-8"],"date":["Thu, 29 May 2025 11:53:05 GMT"],"fastcgi-cache":["BYPASS"],"referrer-policy":["strict-origin-when-cross-origin"],"response-status-code":["200"],"server":["nginx"],"strict-transport-security":["max-age=31536000; includeSubDomains"],"vary":["Accept-Encoding"],"x-content-type-options":["nosniff"],"x-frame-options":["SAMEORIGIN"],"x-xss-protection":["1; mode=block"]}
        """.trimIndent()
        ),
        body = """
            ["Kickstarter +1 hexagon chia leggings, vape authentic flexitarian mukbang live-edge.  Woke succulents cray, tumblr selvage viral prism food truck kogi gochujang drinking vinegar chillwave authentic ramps vape.  Health goth activated charcoal bruh fam.  Semiotics irony prism pickled YOLO fashion axe meh yes plz.  Tacos vegan portland, subway tile sustainable cred pabst godard etsy jawn gentrify.  Shabby chic austin ramps mixtape hot chicken lyft copper mug chartreuse subway tile kitsch waistcoat solarpunk snackwave portland pug.  Live-edge selfies solarpunk, Brooklyn blue bottle church-key pug.","Glossier bicycle rights tacos, big mood post-ironic man bun marfa.  Banh mi asymmetrical messenger bag organic readymade fixie salvia cornhole mukbang try-hard tonx.  Farm-to-table deep v mukbang fingerstache occupy same jawn hot chicken.  Tofu kinfolk yuccie tonx shoreditch selfies freegan scenester put a bird on it polaroid.  Gastropub cloud bread 90's godard deep v portland vape everyday carry kombucha actually.  Pinterest you probably haven't heard of them kale chips, authentic franzen master cleanse trust fund tilde hammock XOXO coloring book biodiesel."]
        """.trimIndent(),
        durationMs = 8348,
        requestBody = ""
    )
    RestApiDetailsUi(restApiDetails)
}

@Preview
@Composable
fun PreviewWsDetailsUi() {
    val wsDetails = WsDetails(
        id = "38b43d8f-b824-49ca-b287-38c548bb1f0c",
        networkType = NetworkType.WEBSOCKET.title,
        timestamp = 1748493681796,
        requestUrl = "https://echo.websocket.events/",
        durationMs = 8348,
        body = "echo.websocket.events sponsored by Lob.com",
        direction = "INCOMING",
        eventType = "MESSAGE",
        messageType = "TEXT",
        messageSize = "42",
        connectionId = "267991855",
    )
    WsDetailsUi(wsDetails)
}

fun parseJsonWithGson(jsonString: String): HashMap<String, List<String>> {
    val type = object : TypeToken<HashMap<String, List<String>>>() {}.type
    return Gson().fromJson(jsonString, type)
}

@Composable
fun DescriptionText(text: String) {
    val isInDarkMode = isSystemInDarkTheme()
    val color = if (isInDarkMode) {
        Color(0xffD1D5DB)
    } else {
        Color(0xff374151)
    }

    Text(text, color = color)
}

@Composable
fun TitleText(text: String) {
    val isInDarkMode = isSystemInDarkTheme()
    val color = if (isInDarkMode) {
        Color(0xff6B7280)
    } else {
        Color(0xff9CA3AF)
    }
    Text(text, color = color, fontWeight = FontWeight.Medium)
}