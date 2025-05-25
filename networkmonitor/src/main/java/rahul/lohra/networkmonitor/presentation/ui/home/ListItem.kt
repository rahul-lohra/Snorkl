package rahul.lohra.networkmonitor.presentation.ui.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rahul.lohra.networkmonitor.NetworkListItem
import rahul.lohra.networkmonitor.RestApiListItem
import rahul.lohra.networkmonitor.WebsocketListItem
import rahul.lohra.networkmonitor.core.TimeUtil


@Composable
fun RestApiListItemUi(
    modifier: Modifier,
    restApiData: RestApiListItem,
    onItemClick: (NetworkListItem) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val restApiMethodColorsScheme = if (isDark) {
        ListItemColorPalettes.dark
    } else {
        ListItemColorPalettes.light
    }

    val restApiMethodColor = when (restApiData.method.capitalize()) {
        "GET" -> restApiMethodColorsScheme.getText
        "PUT" -> restApiMethodColorsScheme.putText
        "POST" -> restApiMethodColorsScheme.postText
        "DELETE" -> restApiMethodColorsScheme.deleteText
        else -> restApiMethodColorsScheme.errorText
    }
    val timeTextColor = if (isDark) Color.White else Color.Black
    val statusCodeColor = when {
        restApiData.responseCode in 200..299 -> StatusCodeItemColorPalettes.success.textColor
        else -> StatusCodeItemColorPalettes.fail.textColor
    }
    Row(modifier = Modifier
        .clickable {
            onItemClick(restApiData)
        }
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth()) {
        Text("${restApiData.responseCode}", color = statusCodeColor, fontWeight = FontWeight.Medium)
        Spacer(Modifier.width(12.dp))
        Column {
            Row {
                Text(
                    modifier = Modifier
                        .background(
                            color = restApiMethodColor.bgColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 4.dp),
                    color = restApiMethodColor.textColor,
                    fontSize = 12.sp,
                    text = restApiData.method.capitalize(), fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    Uri.parse(restApiData.requestUrl).path ?: restApiData.requestUrl,
                    color = timeTextColor
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "\uD83D\uDD12 ${Uri.parse(restApiData.requestUrl).host}",
                color = timeTextColor,
                fontWeight = FontWeight.Light
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(TimeUtil.formatTimestamp(restApiData.timestamp), color = timeTextColor)
                Spacer(Modifier.width(10.dp))
                Text("⏱\uFE0F${restApiData.durationMs / 1000}s", color = timeTextColor)
            }
        }
    }
}

@Composable
fun WebsocketListItemUi(
    modifier: Modifier,
    websocketData: WebsocketListItem,
    onItemClick: (NetworkListItem) -> Unit
) {
    Row(modifier = modifier.clickable {
        onItemClick(websocketData)
    }) {
        Text("Response Code")
        Column {
            Text("Url")
            Text("Host")
            Row {
                Text("${websocketData.timestamp}")
//                Text("${websocketData.durationMs}")
            }
        }
    }
}

@Preview()
@Composable
fun RestApiListItemUiDayPreview() {
    Box(modifier = Modifier.background(Color.White)) {
        val isDark = false
        val restApiMethodColor = ListItemColorPalettes.light.getText
        val timeTextColor = if (isDark) Color.White else Color.Black

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text("200")
            Spacer(Modifier.width(12.dp))
            Column {
                Row {
                    Text(
                        modifier = Modifier
                            .background(
                                restApiMethodColor.bgColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 1.dp, horizontal = 4.dp),
                        color = restApiMethodColor.textColor,
                        fontSize = 12.sp,
                        text = "GET", fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("/api/api", color = timeTextColor)
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "\uD83D\uDD12 hispsum.co",
                    color = timeTextColor,
                    fontWeight = FontWeight.Light
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text("09:07:55 AM", color = timeTextColor)
                    Spacer(Modifier.width(10.dp))
                    Text("⏱\uFE0F 2s", color = timeTextColor)
                }
            }
        }
    }
}

@Preview
@Composable
fun RestApiListItemUiNightPreview() {
    val isDark = true
    val restApiMethodColor = ListItemColorPalettes.dark.getText
    val timeTextColor = if (isDark) Color.White else Color.Black

    Box(modifier = Modifier.background(Color(0xff202A37))) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                "200",
                color = StatusCodeItemColorPalettes.success.textColor,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Row {
                    Text(
                        modifier = Modifier
                            .background(
                                Color(0xff4ADE80),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 1.dp, horizontal = 4.dp),
                        color = restApiMethodColor.textColor,
                        fontSize = 12.sp,
                        text = "GET", fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("/api/api/", color = timeTextColor)
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "\uD83D\uDD12 hispsum.co",
                    fontWeight = FontWeight.Light,
                    color = timeTextColor
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text("09:05:55 PM", color = timeTextColor)
                    Spacer(Modifier.width(10.dp))
                    Text("⏱\uFE0F1s", color = timeTextColor)
                }
            }
        }
    }
}

@Preview(group = "get")
@Composable
fun GetChipsNightPreview() {
    val restApiMethodColor = ListItemColorPalettes.dark.getText

    Row {
        Text(
            modifier = Modifier
                .background(
                    Color(0xff4ADE80),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "GET", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(group = "get")
@Composable
fun GetChipsLightPreview() {
    val restApiMethodColor = ListItemColorPalettes.light.getText
    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "GET", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(group = "put")
@Composable
fun PutChipsDarkPreview() {
    val restApiMethodColor = ListItemColorPalettes.dark.putText
    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "PUT", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(group = "put")
@Composable
fun PutChipsLightPreview() {
    val restApiMethodColor = ListItemColorPalettes.light.putText

    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "PUT", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun PostChipsDarkPreview() {
    val restApiMethodColor = ListItemColorPalettes.dark.postText
    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "POST", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun PostChipsLightPreview() {
    val restApiMethodColor = ListItemColorPalettes.light.postText

    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "POST", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun DeleteChipsDarkPreview() {
    val restApiMethodColor = ListItemColorPalettes.dark.deleteText
    Row() {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "DEL", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun DeleteChipsLightPreview() {
    val restApiMethodColor = ListItemColorPalettes.light.deleteText
    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "DEL", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun WsChipsDarkPreview() {
    val restApiMethodColor = ListItemColorPalettes.dark.wsText
    Row() {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 1.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "WS", fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
fun WsChipsLightPreview() {
    val restApiMethodColor = ListItemColorPalettes.light.wsText
    Row {
        Text(
            modifier = Modifier
                .background(
                    restApiMethodColor.bgColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 0.dp, horizontal = 4.dp),
            color = restApiMethodColor.textColor,
            fontSize = 12.sp,
            text = "WS", fontWeight = FontWeight.SemiBold
        )
    }
}

internal data class NetworkMethodTextColorStyle(val textColor: Color, val bgColor: Color)

internal data class NetworkMethodTextColorScheme(
    val getText: NetworkMethodTextColorStyle,
    val postText: NetworkMethodTextColorStyle,
    val putText: NetworkMethodTextColorStyle,
    val deleteText: NetworkMethodTextColorStyle,
    val errorText: NetworkMethodTextColorStyle,
    val wsText: NetworkMethodTextColorStyle,
)

internal data class NetworkMethodTextColorPalettes(
    val light: NetworkMethodTextColorScheme,
    val dark: NetworkMethodTextColorScheme
)


internal val ListItemColorPalettes = NetworkMethodTextColorPalettes(
    light = NetworkMethodTextColorScheme(
        getText = NetworkMethodTextColorStyle(Color(0xff4ADE80), Color(0xffEFFCF3)),
        postText = NetworkMethodTextColorStyle(Color(0xff60a5fa), Color(0xffF0F6FE)),
        putText = NetworkMethodTextColorStyle(Color(0xfffacc15), Color(0xffFEFAEA)),
        deleteText = NetworkMethodTextColorStyle(Color(0xfff87171), Color(0xffFEF1F1)),
        errorText = NetworkMethodTextColorStyle(Color(0xfff87171), Color(0xffFEF1F1)),
        wsText = NetworkMethodTextColorStyle(Color(0xffc084fc), Color(0xffF9F3FF)),
    ),
    dark = NetworkMethodTextColorScheme(
        getText = NetworkMethodTextColorStyle(Color.White, Color(0xff4ADE80)),
        postText = NetworkMethodTextColorStyle(Color(0xff60a5fa), Color(0xff27384E)),
        putText = NetworkMethodTextColorStyle(Color(0xfffacc15), Color(0xff3A3D33)),
        deleteText = NetworkMethodTextColorStyle(Color(0xfff87171), Color(0xff3A323D)),
        errorText = NetworkMethodTextColorStyle(Color(0xfff87171), Color(0xff3A323D)),
        wsText = NetworkMethodTextColorStyle(Color(0xffc084fc), Color(0xff33344E)),
    ),
)

internal data class StatusCodeTextColorStyle(val textColor: Color)
internal data class StatusCodeTextColorScheme(
    val success: StatusCodeTextColorStyle,
    val fail: StatusCodeTextColorStyle
)

internal val StatusCodeItemColorPalettes = StatusCodeTextColorScheme(
    StatusCodeTextColorStyle(Color(0xff4ADE80)),
    StatusCodeTextColorStyle(Color(0xffF87171))
)