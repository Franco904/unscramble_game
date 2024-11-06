package com.example.unscramble_game.conditionCards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unscramble_game.conditionCards.composables.SimNaoRadioCard
import com.example.unscramble_game.core.presentation.theme.UnscrambleGameTheme

@Composable
fun CidadaoConditionCardDraftScreen(
    modifier: Modifier = Modifier,
) {
    var isValidationTriggered by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary) // white
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        SimNaoRadioCard(
            simNaoAnswer = null,
            isSimNaoAnswerRequired = true,
            mustOpenRadioOnSim = true,
            radioAnswer = null,
            isRadioAnswerRequired = true,
            isValidationTriggered = isValidationTriggered,
            onValidate = { isSimNaoInvalid, simNaoAnswer, isRadioInvalid, radioAnswer ->
                println("Sim/Não answer: $simNaoAnswer, Is invalid? $isSimNaoInvalid | Radio answer: $radioAnswer, Is radio invalid? $isRadioInvalid")
                isValidationTriggered = false
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(onClick = {
            isValidationTriggered = true
        }) {
            Text("Validar")
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
