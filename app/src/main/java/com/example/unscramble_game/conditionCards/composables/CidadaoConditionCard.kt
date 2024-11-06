package com.example.unscramble_game.conditionCards.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unscramble_game.R
import com.example.unscramble_game.conditionCards.CidadaoConditionCardDraftScreen
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme

const val TITLE_EXAMPLE =
    "Você deixou de realizar pequenos trabalhos domésticos, como lavar a louça, arrumar a casa ou fazer limpeza leve?"

@Composable
fun CidadaoConditionCard(
    modifier: Modifier = Modifier,
    title: String,
    contentSecondSection: @Composable () -> Unit,
    contentThirdSection: (@Composable () -> Unit)? = null,
    prefixIcon: (@Composable () -> Unit)? = null,
    isAnswerRequired: Boolean = true,
    isInvalid: Boolean = true,
) {
    val borderColor = if (isInvalid) Color(0xFFB00020) else Color(0xFFBFBFBF)

    Card(
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Column {
            FirstSection(
                titleFirstSection = title,
                isAnswerRequired = isAnswerRequired,
                isInvalid = isInvalid,
                prefixIcon = prefixIcon,
            )
            HorizontalDivider(color = Color(0xFFE4E4E4))
            contentSecondSection()
            HorizontalDivider(color = Color(0xFFE4E4E4))
            AnimatedVisibility(contentThirdSection != null) {
                contentThirdSection?.invoke()
            }
        }
    }
}

@Composable
fun FirstSection(
    titleFirstSection: String,
    modifier: Modifier = Modifier,
    isAnswerRequired: Boolean = true,
    isInvalid: Boolean = true,
    prefixIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .background(Color(0xFFEEEEEE))
            .padding(16.dp)
    ) {
        if (isInvalid) {
            Icon(
                painter = painterResource(R.drawable.ic_invalid),
                contentDescription = null,
                tint = Color(0xFFB00020),
            )
            Spacer(modifier = modifier.width(12.dp))
        } else if (prefixIcon != null) {
            prefixIcon()
            Spacer(modifier = modifier.width(12.dp))
        }
        // font family is Roboto in all texts
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.W500)) {
                    append(titleFirstSection)
                }
                if (isAnswerRequired) {
                    val obrigatorioStyle = SpanStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W400,
                        color = if (isInvalid) Color(0xFFB00020) else Color.Unspecified,
                    )
                    withStyle(obrigatorioStyle) {
                        append(" (Obrigatório)")
                    }
                }
            },
            color = Color(0xFF000000),
        )
    }
}

@Preview
@Composable
fun GamePanelScreenPrevie() {
    UnscrambleGameTheme {
        CidadaoConditionCardDraftScreen()
    }
}
