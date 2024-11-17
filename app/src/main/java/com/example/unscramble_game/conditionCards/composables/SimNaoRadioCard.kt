package com.example.unscramble_game.conditionCards.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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

@Composable
fun SimNaoRadioCard(
    simNaoAnswer: Boolean?,
    isSimNaoAnswerRequired: Boolean,
    mustOpenRadioOnSim: Boolean,
    radioAnswer: String?,
    isRadioAnswerRequired: Boolean,
    isValidationTriggered: Boolean,
    onValidate: (Boolean, Boolean?, Boolean, String?) -> Unit,
    modifier: Modifier = Modifier,
    title: String = TITLE_EXAMPLE,
) {
    var currentSimNaoAnswer by remember { mutableStateOf(simNaoAnswer) }
    var currentRadioAnswer by remember { mutableStateOf(radioAnswer) }

    var isSimNaoInvalid by remember { mutableStateOf(false) }
    var isRadioInvalid by remember { mutableStateOf(false) }

    var isThirdSectionOpened by remember { mutableStateOf(
        (!mustOpenRadioOnSim && simNaoAnswer == false) || (mustOpenRadioOnSim && simNaoAnswer == true)
    ) }

    LaunchedEffect(isValidationTriggered) {
        if (isValidationTriggered) {
            isSimNaoInvalid = isSimNaoAnswerRequired && currentSimNaoAnswer == null
            isRadioInvalid = isThirdSectionOpened && isRadioAnswerRequired && currentRadioAnswer == null

            onValidate(isSimNaoInvalid, currentSimNaoAnswer, isRadioInvalid, currentRadioAnswer)
        }
    }

    QuestionCard(
        title = title,
        contentSecondSection = {
            ThreeButtonsSection(
                currentAnswer = currentSimNaoAnswer,
                onLimparPress = {
                    isThirdSectionOpened = false
                    currentSimNaoAnswer = null
                    currentRadioAnswer = null

                    isSimNaoInvalid = false
                    isRadioInvalid = false
                },
                onNaoPress = {
                    isThirdSectionOpened = !mustOpenRadioOnSim
                    currentSimNaoAnswer = false
                    if (mustOpenRadioOnSim)
                        currentRadioAnswer = null

                    isSimNaoInvalid = false
                    isRadioInvalid = false
                },
                onSimPress = {
                    isThirdSectionOpened = mustOpenRadioOnSim
                    currentSimNaoAnswer = true
                    if (!mustOpenRadioOnSim)
                        currentRadioAnswer = null

                    isSimNaoInvalid = false
                    isRadioInvalid = false
                },
            )
        },
        contentThirdSection = if (isThirdSectionOpened) {
            {
                RadioGroupSection(
                    currentAnswer = currentRadioAnswer,
                    onRadioAnswer = { radioAnswer ->
                        currentRadioAnswer = radioAnswer
                        isRadioInvalid = false
                    },
                    isAnswerRequired = isRadioAnswerRequired,
                    isInvalid = isRadioInvalid,
                )
            }
        } else null,
        isAnswerRequired = isSimNaoAnswerRequired,
        isInvalid = isSimNaoInvalid,
        // Not applied for this CidadaoConditionCard type
//        prefixIcon = {
//            Icon(
//                painter = painterResource(R.drawable.ic_add_condition),
//                contentDescription = null,
//            )
//        },
        modifier = modifier
    )
}

@Composable
private fun ThreeButtonsSection(
    onLimparPress: () -> Unit,
    onNaoPress: () -> Unit,
    onSimPress: () -> Unit,
    modifier: Modifier = Modifier,
    currentAnswer: Boolean?,
) {
    var isNaoPressed by remember { mutableStateOf(currentAnswer != null && !currentAnswer) }
    var isSimPressed by remember { mutableStateOf(currentAnswer != null && currentAnswer) }

    val areBothNotPressed = !isNaoPressed && !isSimPressed

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF))
    ) {
        ConditionCardButton(
            text = "LIMPAR",
            onClick = {
                isNaoPressed = false
                isSimPressed = false
                onLimparPress()
            },
            textColor = Color(0xFF4A4A4A),
            textFontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 2.dp),
            textModifier = Modifier
                .background(Color(0xFFFFFFFF))
        )
        Row {
            ConditionCardButton(
                text = "NÃO",
                onClick = {
                    isNaoPressed = true
                    isSimPressed = false
                    onNaoPress()
                },
                buttonBorder = if (isNaoPressed) BorderStroke((1.5).dp, Color(0xFF217B00)) else null,
                textColor = if (areBothNotPressed || isNaoPressed) Color(0xFF217B00) else Color(0xFF4A4A4A),
                textFontWeight = if (isNaoPressed) FontWeight.W500 else FontWeight.W400,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                textModifier = Modifier
                    .background(if (isNaoPressed) Color(0xFFE5F1E1) else Color(0xFFFFFFFF))
            )
            ConditionCardButton(
                text = "SIM",
                onClick = {
                    isNaoPressed = false
                    isSimPressed = true
                    onSimPress()
                },
                buttonBorder = if (isSimPressed) BorderStroke((1.5).dp, Color(0xFF217B00)) else null,
                textColor = if (areBothNotPressed || isSimPressed) Color(0xFF217B00) else Color(0xFF4A4A4A),
                textFontWeight = if (isSimPressed) FontWeight.W500 else FontWeight.W400,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                textModifier = Modifier
                    .background(if (isSimPressed) Color(0xFFE5F1E1) else Color(0xFFFFFFFF))
            )
        }
    }
}

@Composable
fun ConditionCardButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color,
    textFontWeight: FontWeight,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonBorder: BorderStroke? = null,
) {
    Surface(
        onClick = onClick,
        border = buttonBorder,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Text(
            text,
            letterSpacing = 1.5.sp,
            color = textColor,
            fontWeight = textFontWeight,
            modifier = textModifier
                .padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun RadioGroupSection(
    currentAnswer: String?,
    onRadioAnswer: (String) -> Unit,
    isInvalid: Boolean,
    isAnswerRequired: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF))
            .padding(16.dp)
    ) {
        Row {
            if (isInvalid) {
                Icon(
                    painter = painterResource(R.drawable.ic_invalid),
                    contentDescription = null,
                    tint = Color(0xFFB00020),
                )
                Spacer(modifier = modifier.width(12.dp))
            }
            // font family is Roboto in all texts
            Text(
                text = buildAnnotatedString {
                    val groupTitleStyle = SpanStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = if (isInvalid) Color(0xFFB00020) else Color(0xFF000000).copy(alpha = 0.87f),
                    )
                    withStyle(groupTitleStyle) {
                        append("Qual tipo?")
                    }

                    if (isAnswerRequired) {
                        val obrigatorioStyle = SpanStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            color = if (isInvalid) Color(0xFFB00020) else Color(0xFF000000).copy(alpha = 0.54f),
                        )
                        withStyle(obrigatorioStyle) {
                            append(" (Obrigatório)")
                        }
                    }
                },
                color = Color(0xFF000000),
            )
        }
        CondicoesRadioGroup(
            currentAnswer = currentAnswer,
            onRadioAnswer = onRadioAnswer,
        )
    }
}

@Composable
private fun CondicoesRadioGroup(
    currentAnswer: String?,
    onRadioAnswer: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val radioOptions = listOf("Lavar louça", "Arrumar a casa", "Fazer limpeza leve")
    var selectedOption by remember { mutableStateOf(currentAnswer ?: "") }

    Column(
        modifier = modifier
            .selectableGroup()
    ) {
        for (radioOption in radioOptions) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(top = 16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            selectedOption = radioOption
                            onRadioAnswer(radioOption)
                        }
                    }
            ) {
                RadioButton(
                    selected = radioOption == selectedOption,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF2F8C10),
                        unselectedColor = Color(0xFF000000).copy(alpha = 0.54f),
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    radioOption,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400, // normal
                    color = Color(0xFF000000).copy(alpha = 0.87f),
                )
            }
        }
    }
}

@Preview
@Composable
fun GamePanelScreenPreview() {
    UnscrambleGameTheme {
        CidadaoConditionCardDraftScreen()
    }
}
