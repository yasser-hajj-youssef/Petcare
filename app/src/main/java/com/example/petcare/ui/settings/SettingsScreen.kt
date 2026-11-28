package com.example.petcare.ui.settings

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.notify.canPostNotifications
import com.example.petcare.data.ThemeMode


@Composable
fun SettingsRoute(

    onBack: () -> Unit,

    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory
    )

) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle()


    SettingsScreen(

        state = state,

        onBack = onBack,

        onSetRemindersEnabled =
            viewModel::setRemindersEnabled,

        onPermissionResult =
            viewModel::onPermissionResult,

        onThemeChange =
            viewModel::setThemeMode
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(

    state: SettingsUiState,

    onBack: () -> Unit,

    onSetRemindersEnabled: (Boolean) -> Unit,

    onPermissionResult: (Boolean) -> Unit,

    onThemeChange: (ThemeMode) -> Unit

) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current


    var notificationsAllowed by remember {

        mutableStateOf(
            NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()
        )
    }


    DisposableEffect(lifecycleOwner, context) {

        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME) {

                notificationsAllowed =
                    NotificationManagerCompat
                        .from(context)
                        .areNotificationsEnabled()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {

            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val launcher = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.RequestPermission()

    ) { granted ->

        notificationsAllowed =
            NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()

        onPermissionResult(
            granted && notificationsAllowed
        )
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Settings")
                },

                navigationIcon = {

                    TextButton(onClick = onBack) {

                        Text("Back")
                    }
                }
            )
        }

    ) { padding ->


        Column(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {


            // Notifications section

            Text(
                text = "Notifications",

                style = MaterialTheme.typography.titleMedium
            )


            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(

                    modifier = Modifier.padding(16.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)

                ) {

                    Row(

                        modifier = Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Care reminders",

                                style = MaterialTheme
                                    .typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Receive reminders for your pets' care routines.",

                                style = MaterialTheme
                                    .typography.bodySmall
                            )
                        }


                        Switch(

                            checked = state.remindersEnabled,

                            onCheckedChange = { enabled ->

                                if (!enabled) {

                                    onSetRemindersEnabled(false)

                                } else if (
                                    Build.VERSION.SDK_INT >=
                                    Build.VERSION_CODES.TIRAMISU &&
                                    !canPostNotifications(context)
                                ) {

                                    launcher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )

                                } else if (!notificationsAllowed) {

                                    onPermissionResult(false)

                                } else {

                                    onSetRemindersEnabled(true)
                                }
                            }
                        )
                    }


                    if (
                        !notificationsAllowed ||
                        state.permissionDenied
                    ) {

                        HorizontalDivider()


                        Text(

                            text = "Notifications are disabled. Allow them in your phone settings to receive reminders.",

                            color = MaterialTheme.colorScheme.error,

                            style = MaterialTheme.typography.bodyMedium
                        )


                        OutlinedButton(

                            modifier = Modifier.fillMaxWidth(),

                            onClick = {

                                val intent = Intent(
                                    Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                ).apply {

                                    putExtra(
                                        Settings.EXTRA_APP_PACKAGE,
                                        context.packageName
                                    )
                                }

                                context.startActivity(intent)
                            }

                        ) {

                            Text("Open notification settings")
                        }
                    }
                }
            }


            Text(

                text = "Reminders may arrive a few minutes after their scheduled time.",

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.onSurfaceVariant
            )


            // Appearance section

            Text(

                text = "Appearance",

                style = MaterialTheme.typography.titleMedium
            )


            ThemeSelectionCard(

                selectedTheme = state.themeMode,

                onThemeChange = onThemeChange
            )
        }
    }
}


@Composable
fun ThemeSelectionCard(

    selectedTheme: ThemeMode,

    onThemeChange: (ThemeMode) -> Unit

) {

    Card(

        modifier = Modifier.fillMaxWidth()

    ) {

        Column(

            modifier = Modifier
                .padding(16.dp)
                .selectableGroup(),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)

        ) {

            Text(

                text = "App theme",

                style = MaterialTheme.typography.titleMedium
            )


            Text(

                text = "Choose how PetCare looks on your device.",

                style = MaterialTheme.typography.bodySmall
            )


            ThemeOption(

                title = "System default",

                description = "Follow your phone's appearance",

                selected = selectedTheme == ThemeMode.SYSTEM,

                onClick = {
                    onThemeChange(ThemeMode.SYSTEM)
                }
            )


            HorizontalDivider()


            ThemeOption(

                title = "Light mode",

                description = "Cream and coral appearance",

                selected = selectedTheme == ThemeMode.LIGHT,

                onClick = {
                    onThemeChange(ThemeMode.LIGHT)
                }
            )


            HorizontalDivider()


            ThemeOption(

                title = "Dark mode",

                description = "Navy and coral appearance",

                selected = selectedTheme == ThemeMode.DARK,

                onClick = {
                    onThemeChange(ThemeMode.DARK)
                }
            )
        }
    }
}


@Composable
fun ThemeOption(

    title: String,

    description: String,

    selected: Boolean,

    onClick: () -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {

        Column(

            modifier = Modifier.weight(1f)

        ) {

            Text(

                text = title,

                style = MaterialTheme.typography.bodyLarge
            )


            Text(

                text = description,

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        RadioButton(

            selected = selected,

            onClick = null
        )
    }
}