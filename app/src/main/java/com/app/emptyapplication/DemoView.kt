package com.app.emptyapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun DemoView(inDemoVM: DemoVM )
{
    val currentCount: Int by inDemoVM.count.observeAsState(initial = 0)

    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Count: $currentCount")

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                inDemoVM.IncreaseCount()
            }
        ) {
            Text(text = "(+1)")
        }
    }
}