package org.datepollsystems.waiterrobot.mediator.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.datepollsystems.waiterrobot.mediator.App
import org.datepollsystems.waiterrobot.mediator.ui.common.DemoEventInfo
import org.datepollsystems.waiterrobot.mediator.ui.common.SelectedEnvironmentInfo
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(vm: MainScreenViewModel) {
    val state = vm.state.collectAsState().value
    val isConnected = App.socketManager.isConnected.collectAsState(initial = false).value

    Column {
        SelectedEnvironmentInfo()
        DemoEventInfo(vm.isDemoEvent)
        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Running...",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )

            Icon(
                if (isConnected) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                contentDescription = "Socket Connected",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        Divider(thickness = 3.dp)

        Row {
            // Transaction log
            Column(modifier = Modifier.weight(2f)) {
                // TODO show a transaction log
                if (state.printTransactions.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(state.printTransactions.items, key = PrintTransaction::id) {
                            Row(modifier = Modifier.padding(10.dp)) {
                                Text(it.time.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = it.jobName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = it.printer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Divider()
                        }
                    }
                }
            }

            // Printer list
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.printers, key = { it.first }) { (_, pairing) ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${pairing.bePrinter.name} (${pairing.loPrinter.name})",
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(onClick = { vm.printTestPdf(pairing.bePrinter.id) }) {
                            Icon(Icons.Filled.Print, "Execute test print")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}
