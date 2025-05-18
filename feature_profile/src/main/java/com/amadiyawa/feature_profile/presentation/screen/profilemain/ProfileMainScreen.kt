package com.amadiyawa.feature_profile.presentation.screen.profilemain

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amadiyawa.feature_base.presentation.compose.composable.ButtonIconType
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButton
import com.amadiyawa.feature_base.presentation.compose.composable.CircularButtonParams
import com.amadiyawa.feature_base.presentation.compose.composable.ErrorScreen
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodyMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextBodySmall
import com.amadiyawa.feature_base.presentation.compose.composable.TextHeadlineSmall
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_base.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_base.presentation.compose.composable.ToolbarParams
import com.amadiyawa.feature_profile.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMainScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileMainViewModel = koinViewModel(),
    onEditProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileMainContract.Effect.NavigateToEditProfile -> {
                    onEditProfileClick()
                }
                is ProfileMainContract.Effect.NavigateToSettings -> {
                    onSettingsClick()
                }
                is ProfileMainContract.Effect.NavigateToAbout -> {
                    onAboutClick()
                }
                is ProfileMainContract.Effect.UserSignedOut -> {
                    onSignOutClick()
                }
                is ProfileMainContract.Effect.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ProfileMainContract.Effect.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                state = state,
                onRefreshClick = {
                    viewModel.dispatch(ProfileMainContract.Action.RefreshUserProfile)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isLoading && state.userName.isNotEmpty(),
            onRefresh = {
                viewModel.dispatch(ProfileMainContract.Action.RefreshUserProfile)
            },
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading && state.userName.isEmpty() -> {
                    // Initial loading
                    LoadingAnimation(visible = true)
                }
                state.error != null && state.userName.isEmpty() -> {
                    // Error state with no data
                    ErrorScreen(
                        error = state.error ?: stringResource(R.string.unknown_error),
                        onRetry = {
                            viewModel.dispatch(ProfileMainContract.Action.LoadUserProfile)
                        }
                    )
                }
                else -> {
                    // Content state
                    ProfileContent(
                        state = state,
                        onAction = viewModel::dispatch,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTopBar(
    state: ProfileMainContract.State,
    onRefreshClick: () -> Unit
) {
    Toolbar(
        params = ToolbarParams(
            title = stringResource(R.string.profile)
        ),
        actions = {
            // Refresh button
            CircularButton(
                params = CircularButtonParams(
                    iconType = ButtonIconType.Vector(Icons.Default.Refresh),
                    onClick = onRefreshClick,
                    description = stringResource(R.string.refresh),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    iconTint = MaterialTheme.colorScheme.primary,
                    enabled = !state.isLoading
                )
            )
        }
    )
}

@Composable
private fun ProfileContent(
    state: ProfileMainContract.State,
    onAction: (ProfileMainContract.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Profile Header
        ProfileHeader(
            userName = state.userName,
            userEmail = state.userEmail!!,
            userRole = state.userRole.name,
            avatarUrl = state.userAvatarUrl,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Actions
        ProfileActionsSection(
            state = state,
            onAction = onAction
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error message if any (for partial errors)
        AnimatedVisibility(
            visible = state.error != null && state.userName.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            state.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextBodyMedium(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    userName: String,
    userEmail: String,
    userRole: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.profile_avatar),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop,
                fallback = painterResource(R.drawable.ic_default_avatar),
                error = painterResource(R.drawable.ic_default_avatar)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            TextHeadlineSmall(
                text = userName.ifEmpty { stringResource(R.string.unknown_user) },
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email
            TextBodyMedium(
                text = userEmail,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Role chip
            ProfileRoleChip(
                role = userRole,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ProfileRoleChip(
    role: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        TextBodySmall(
            text = role.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ProfileActionsSection(
    state: ProfileMainContract.State,
    onAction: (ProfileMainContract.Action) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            // Edit Profile
            if (state.hasEditProfileAccess) {
                ProfileActionItem(
                    icon = Icons.Default.Edit,
                    title = stringResource(R.string.edit_profile),
                    subtitle = stringResource(R.string.edit_profile_description),
                    onClick = { onAction(ProfileMainContract.Action.EditProfileClicked) }
                )
            }

            // Settings
            if (state.hasSettingsAccess) {
                ProfileActionItem(
                    icon = Icons.Default.Settings,
                    title = stringResource(R.string.settings),
                    subtitle = stringResource(R.string.settings_description),
                    onClick = { onAction(ProfileMainContract.Action.SettingsClicked) }
                )
            }

            // About
            ProfileActionItem(
                icon = Icons.Default.Info,
                title = stringResource(R.string.about),
                subtitle = stringResource(R.string.about_description),
                onClick = { onAction(ProfileMainContract.Action.AboutClicked) }
            )

            // Divider before sign out
            if (state.hasSignOutAccess) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Sign Out
                ProfileActionItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = stringResource(R.string.sign_out),
                    subtitle = stringResource(R.string.sign_out_description),
                    onClick = { onAction(ProfileMainContract.Action.SignOutClicked) },
                    isDestructive = true
                )
            }
        }
    }
}

@Composable
private fun ProfileActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (isDestructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            TextTitleMedium(
                text = title,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            TextBodySmall(
                text = subtitle,
                color = if (isDestructive) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                }
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}