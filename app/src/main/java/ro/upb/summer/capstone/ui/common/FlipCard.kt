package ro.upb.summer.capstone.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ro.upb.summer.capstone.domain.Card as CardModel

/**
 * A single flashcard that flips on tap: question on the front, answer on the
 * back. The back face is counter-rotated so its text isn't mirrored.
 */
@Composable
fun FlipCard(
    card: CardModel,
    modifier: Modifier = Modifier,
) {
    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip",
    )
    val showingFront = rotation <= 90f

    Card(
        onClick = { flipped = !flipped },
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = if (showingFront) "Showing question" else "Showing answer"
            }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        colors = CardDefaults.cardColors(
            containerColor = if (showingFront) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer
            },
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 96.dp)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (showingFront) {
                Text(
                    text = card.question,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = card.answer,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                )
            }
        }
    }
}