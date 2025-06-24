package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amadiyawa.feature_base.domain.model.Country
import com.amadiyawa.feature_base.presentation.theme.dimension

@Composable
fun CountryCodeSelector(
    selectedCountry: Country,
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.width(80.dp)) {
        Row(
            modifier = modifier
                .widthIn(min = 72.dp)
                .clickable { expanded = true }
                .padding(start = MaterialTheme.dimension.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.spacing.xSmall)
        ) {
            // Flag emoji
            Text(
                text = selectedCountry.flagEmoji,
                fontSize = 24.sp
            )

            // Dropdown icon
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select country",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(MaterialTheme.dimension.componentSize.iconSmall)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.widthIn(min = 220.dp)
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimension.spacing.small)
                            ) {
                                Text(
                                    text = country.flagEmoji,
                                    fontSize = 20.sp
                                )

                                TextBodyMedium(
                                    text = stringResource(id = country.nameResId)
                                )

                                TextBodyMedium(
                                    text = country.dialCode,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}