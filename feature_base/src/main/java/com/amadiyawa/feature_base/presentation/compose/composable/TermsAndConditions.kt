package com.amadiyawa.feature_base.presentation.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.amadiyawa.droidkotlin.base.R

data class TermsAndConditionsTextData(
    val preText: String,
    val termsLabel: String,
    val privacyLabel: String,
    val separator: String,
    val postText: String,
    val termsUrl: String,
    val privacyUrl: String
)

@Composable
fun defaultTermsAndConditionsTextData(): TermsAndConditionsTextData {
    return TermsAndConditionsTextData(
        preText = stringResource(R.string.tos_pre_text),
        termsLabel = stringResource(R.string.tos_terms),
        separator = stringResource(R.string.tos_separator),
        privacyLabel = stringResource(R.string.tos_privacy),
        postText = stringResource(R.string.tos_post_text),
        termsUrl = "https://github.com/amadiyawa",
        privacyUrl = "https://x.com/amadiyawa"
    )
}

@Composable
fun TermsAndConditions(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    data: TermsAndConditionsTextData = defaultTermsAndConditionsTextData()
) {

    val annotatedText = buildAnnotatedString {
        append(data.preText + " ")

        withLink(LinkAnnotation.Url(data.termsUrl)) {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(data.termsLabel)
            }
        }

        append(" " + data.separator + " ")

        withLink(LinkAnnotation.Url(data.privacyUrl)) {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(data.privacyLabel)
            }
        }

        append(data.postText)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = annotatedText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth().clickable {

            },
            onTextLayout = {},
            overflow = TextOverflow.Clip
        )
    }
}
