package com.amadiyawa.feature_users.presentation.screen.userdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amadiyawa.feature_base.common.res.Dimen
import com.amadiyawa.feature_base.common.util.formatDobString
import com.amadiyawa.feature_base.presentation.compose.composable.DataNotFoundAnim
import com.amadiyawa.feature_base.presentation.compose.composable.DrawHorizontalDottedLine
import com.amadiyawa.feature_base.presentation.compose.composable.ExpandableRow
import com.amadiyawa.feature_base.presentation.compose.composable.LoadingAnimation
import com.amadiyawa.feature_base.presentation.compose.composable.PlaceholderImage
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleLarge
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleMedium
import com.amadiyawa.feature_base.presentation.compose.composable.TextTitleSmall
import com.amadiyawa.feature_base.presentation.compose.composable.Toolbar
import com.amadiyawa.feature_user.R
import com.amadiyawa.feature_users.domain.model.Dob
import com.amadiyawa.feature_users.domain.model.Id
import com.amadiyawa.feature_users.domain.model.Location
import com.amadiyawa.feature_users.domain.model.Registered
import com.amadiyawa.feature_users.domain.model.User
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserDetailScreen(
    onBackClick: () -> Unit,
    uuid: String
) {
    val viewModel: UserDetailViewModel = koinViewModel()
    viewModel.onEnter(uuid)

    Scaffold(
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = { SetUpToolbar(onBackClick) },
    ) { paddingValues ->
        SetupContent(
            viewModel = viewModel,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun SetUpToolbar(onBackClick: () -> Unit) {
    Toolbar(centered = true, title = "", onBackClick = onBackClick)
}

@Composable
private fun SetupContent(
    viewModel: UserDetailViewModel,
    paddingValues: PaddingValues,
) {
    val uiState: UserDetailViewModel.UiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        HandleUiState(uiState = uiState)
    }
}

@Composable
private fun HandleUiState(
    uiState: UserDetailViewModel.UiState,
) {
    uiState.let {
        when (it) {
            UserDetailViewModel.UiState.Error -> {
                DataNotFoundAnim()
            }
            UserDetailViewModel.UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingAnimation(visible = true)
                }
            }
            is UserDetailViewModel.UiState.Content -> {
                val user = it.user
                UserDetail(user = user)
            }
        }
    }
}

@Composable
private fun UserDetail(user: User) {
    val scrollState = rememberScrollState()

    UserOverview(user)

    Spacer(modifier = Modifier.height(Dimen.Spacing.medium))

    Column(
        modifier = Modifier
        .padding(start = Dimen.Spacing.small, end = Dimen.Spacing.small)
        .verticalScroll(scrollState)
    ) {
        LocationCard(user.location)
        DobCard(userDob = user.dob)
        RegisteredCard(userRegistered = user.registered)
        IdCard(userId = user.id)
        ContactCard(user = user)
    }
}

@Composable
private fun UserOverview(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(start = Dimen.Spacing.large, bottom = 26.dp),
        horizontalArrangement = Arrangement.spacedBy(Dimen.Spacing.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.size(Dimen.Picture.mediumSize)) {
            PlaceholderImage(
                url = user.picture.large,
                contentDescription = user.picture.thumbnail,
                gender = user.gender,
                size = Dimen.Picture.mediumSize
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            TextTitleLarge(
                text = user.name.title + " " + user.name.first + " " + user.name.last,
            )

            TextTitleSmall(text = "@" + user.login.username)
        }
    }
}

@Composable
private fun LocationCard(userLocation: Location) {
    var expanded by remember { mutableStateOf (false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextTitleMedium(
                    text = stringResource(id = R.string.location),
                    fontWeight = FontWeight.Bold
                )
            }

            DrawHorizontalDottedLine(
                color = MaterialTheme.colorScheme.onSurface,
                startPadding = 0.dp,
                endPadding = 0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.city),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userLocation.city,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.state),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userLocation.state,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.country),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userLocation.country,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.postcode),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userLocation.postcode,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.background)

            ExpandableRow(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                expanded = expanded
            ) {
                expanded = !expanded
            }

            AnimatedVisibility(visible = expanded) {
                MoreLocationInfo(userLocation = userLocation)
            }
        }
    }
}

@Composable
private fun MoreLocationInfo(userLocation: Location) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.street),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = userLocation.street.name + " " + userLocation.street.number,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            Text(
                text = stringResource(id = R.string.coordinates),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = userLocation.coordinates.latitude + " " + userLocation.coordinates.longitude,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            Text(
                text = stringResource(id = R.string.timezone),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = userLocation.timezone.offset + " " + userLocation.timezone.description,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DobCard(userDob: Dob) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextTitleMedium(
                    text = stringResource(id = R.string.date_of_birth),
                    fontWeight = FontWeight.Bold
                )
            }

            DrawHorizontalDottedLine(
                color = MaterialTheme.colorScheme.onSurface,
                startPadding = 0.dp,
                endPadding = 0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            ) {
                Row {
                    Text(
                        modifier = Modifier,
                        text = formatDobString(userDob.date),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        modifier = Modifier,
                        text = userDob.age.toString() + " " + stringResource(id = R.string.years_old),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisteredCard(userRegistered: Registered) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextTitleMedium(
                    text = stringResource(id = R.string.registered),
                    fontWeight = FontWeight.Bold
                )
            }

            DrawHorizontalDottedLine(
                color = MaterialTheme.colorScheme.onSurface,
                startPadding = 0.dp,
                endPadding = 0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            ) {
                Row {
                    Text(
                        modifier = Modifier,
                        text = formatDobString(userRegistered.date),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        modifier = Modifier,
                        text = userRegistered.age.toString() + " " + stringResource(id = R.string.seniority),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun IdCard(userId: Id) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextTitleMedium(
                    text = stringResource(id = R.string.id_card),
                    fontWeight = FontWeight.Bold
                )
            }

            DrawHorizontalDottedLine(
                color = MaterialTheme.colorScheme.onSurface,
                startPadding = 0.dp,
                endPadding = 0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            ) {
                Row {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.name),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userId.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.value),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = userId.value,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextTitleMedium(
                    text = stringResource(id = R.string.contact),
                    fontWeight = FontWeight.Bold
                )
            }

            DrawHorizontalDottedLine(
                color = MaterialTheme.colorScheme.onSurface,
                startPadding = 0.dp,
                endPadding = 0.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.email),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.phone),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = user.phone,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Text(
                        text = stringResource(id = R.string.cell),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = user.cell,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}