package org.datepollsystems.waiterrobot.mediator.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DemoEventInfo(isDemoEvent: Boolean?) {
    if (isDemoEvent != true) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Red.copy(alpha = 0.9f),
        contentColor = Color.Black
    ) {
        Text(
            text = "The selected Event is a Demo-Event. Orders will not be printed completely.",
            modifier = Modifier.padding(5.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
