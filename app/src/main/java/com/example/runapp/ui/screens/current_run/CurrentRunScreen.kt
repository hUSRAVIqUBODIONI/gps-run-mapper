package com.example.runapp.ui.screens.current_run

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.runapp.data.location.LocationData
import com.example.runapp.location.LocationService
import com.example.runapp.ui.theme.PurpleGrey80
import com.example.runapp.utils.TimerScreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber
import kotlin.math.log10


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CurrentRunScreen(
    viewModel: CurrentRunViewModel = hiltViewModel(),
    navController: NavController,
    innerPaddingValues: PaddingValues
) {

    val path by viewModel.path.collectAsState()
    val parsedTime by viewModel.parsedTime.collectAsState()
    val timerStarted = viewModel.timerStarted.collectAsState()


    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val lat = intent?.getDoubleExtra("lat", 0.0)
                val lon = intent?.getDoubleExtra("lon", 0.0)
                val tmp = intent?.getLongExtra("tmp",0L)
                Timber.tag("MyLocation").d("$lat - $lon - $tmp")
                viewModel.addLocation(
                    lat = lat!!,
                    lon = lon!!,
                    tmp = tmp!!,
                )
            }
        }

        val filter = IntentFilter("LOCATION_UPDATE")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(
                receiver,
                filter,
                Context.RECEIVER_EXPORTED // или NOT_EXPORTED
            )
        } else {
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

        // отписка
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val activity = context as? Activity

    var hasLocationPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }




    var shouldShowDialog by remember { mutableStateOf(false) }
    var permanentDenied by remember { mutableStateOf(false) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        hasLocationPermission.value = isGranted

        if (!isGranted) {
            permanentDenied = activity?.shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == false
            shouldShowDialog = true
        }
    }



    LaunchedEffect(key1 = 1) {
        if (!hasLocationPermission.value) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    if (shouldShowDialog) {

        PermissionDialog(
            onDismiss = { shouldShowDialog = false },
            onConfirm = {
                shouldShowDialog = false
                if (permanentDenied) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startForegroundService(intent)
                } else {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PurpleGrey80),
            contentAlignment = Alignment.Center
        ) {
            if (hasLocationPermission.value) {
                MapScreen(path)
            } else {
                Text("Ne ok")
            }

        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerScreen(
                parsedTime,
                timerStarted.value
            )
            Row {
                Button(
                    onClick = {
                        val startIntent = Intent(context, LocationService::class.java).apply {
                            action = LocationService.ACTION_START
                        }
                        context.startService(startIntent)
                        viewModel.startTimer()
                    },
                    enabled = !timerStarted.value
                ) {
                    Text(text = "Start")
                }
                Button(
                    onClick = {
                        val stopIntent = Intent(context, LocationService::class.java).apply {
                            action = LocationService.ACTION_STOP
                        }
                        context.stopService(stopIntent)
                        viewModel.stopTimer()
                    },
                    enabled = timerStarted.value
                ) {
                    Text("Finish")
                }
            }

        }

    }
}


@Composable
fun PermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Доступ к геолокации") },
        text = { Text("Приложению требуется доступ к вашему местоположению для корректной работы.") },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onConfirm) {
                Text("Разрешить")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Отклонить")
            }
        }
    )
}


@Composable
fun MapScreen(
    path: List<LocationData>
) {

    if (path.size != 0) {
        val _startLocation = LatLng(path.first().latitude, path.first().longitude)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(_startLocation, 50f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (path.size > 1) {
                Polyline(
                    points = path.map { LatLng(it.latitude, it.longitude) },
                    color = Color.Red,
                    width = 5f
                )
            }

            path.lastOrNull()?.let {

                Marker(state = MarkerState(LatLng(it.latitude, it.longitude)))
            }

        }
    }

}