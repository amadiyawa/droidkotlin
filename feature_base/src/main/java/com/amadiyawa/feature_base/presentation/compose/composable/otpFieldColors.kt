package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amadiyawa.feature_base.domain.model.ValidatedField

@Composable
fun otpFieldColors(field: ValidatedField<String>): TextFieldColors {
    val isValid = field.isTouched && field.validation.isValid
    val isInvalid = field.isTouched && !field.validation.isValid

    return OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = when {
            isValid -> MaterialTheme.colorScheme.primary
            isInvalid -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.outline
        },
        focusedBorderColor = when {
            isValid -> MaterialTheme.colorScheme.primary
            isInvalid -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        },
        errorBorderColor = MaterialTheme.colorScheme.error,
        focusedContainerColor = if (isValid) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent,
        unfocusedContainerColor = if (isValid) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else Color.Transparent
    )
}
