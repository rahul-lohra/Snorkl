package rahul.lohra.networkmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rahul.lohra.networkinspector.Util
import rahul.lohra.networkmonitor.ui.theme.AndroidNetworkMonitorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidNetworkMonitorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize() // Make the Column take up the entire screen
            .padding(16.dp), // Optional padding around the content
        verticalArrangement = Arrangement.Center, // Center items vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
    ) {
        val context = LocalContext.current
        val ipAddress = Util.getLocalIpAddress(context)
        Text(
            text = "Phone IP: $ipAddress",
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            RestClient.makeRequest()
        }) {
            Text("Make HTTP Call")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            WebsocketClient.connectAndTest()
        }) {
            Text("Start WS")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(onClick = {
            WebsocketClient.sendMessage("Hello From Rahul")
        }) {
            Text("Send WS events")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(onClick = {
            WebsocketClient.close()
        }) {
            Text("Close WS events")
        }


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidNetworkMonitorTheme {
        Greeting("Android")
    }
}