package rahul.lohra.networkmonitor.presentation.ui.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    Row(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .clickable {
            onItemClick(restApiData)
        }
        .fillMaxWidth()) {
        Text("${restApiData.responseCode}")
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
                    color = Color(0xffffffff),
                    fontSize = 12.sp,
                    text = restApiData.method.capitalize(), fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(4.dp))
                Text(Uri.parse(restApiData.requestUrl).path ?: restApiData.requestUrl)
            }
            Text(
                "\uD83D\uDD12 ${Uri.parse(restApiData.requestUrl).host}",
                fontWeight = FontWeight.Light
            )
            Row {
                Text(TimeUtil.formatTimestamp(restApiData.timestamp))
                Spacer(Modifier.width(10.dp))
                Text("⏱\uFE0F${restApiData.durationMs/1000}s")
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

@Preview
@Composable
fun RestApiListItemUiDayPreview() {
    Box(modifier = Modifier.background(Color.White)){
        Row(modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)

            .fillMaxWidth()) {
            Text("200")
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
                        color = Color(0xffffffff),
                        fontSize = 12.sp,
                        text = "GET", fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("/api/api/")
                }
                Text(
                    "\uD83D\uDD12 hispsum.co",
                    fontWeight = FontWeight.Light
                )
                Row {
                    Text("09:05:55 PM")
                    Spacer(Modifier.width(10.dp))
                    Text("⏱\uFE0F1s")
                }
            }
        }
    }

}

@Preview
@Composable
fun RestApiListItemUiNightPreview() {
    Box(modifier = Modifier.background(Color(0xff202A37))){
        Row(modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)

            .fillMaxWidth()) {
            Text("200", color = Color(0xff4ADE80), fontWeight = FontWeight.Medium)
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
                        color = Color(0xffffffff),
                        fontSize = 12.sp,
                        text = "GET", fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("/api/api/", color = Color.White)
                }
                Text(modifier = Modifier.padding(top = 8.dp),
                    text = "\uD83D\uDD12 hispsum.co",
                    fontWeight = FontWeight.Light,
                    color = Color.White
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Text("09:05:55 PM", color = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Text("⏱\uFE0F1s", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TimerText() {

}