package ro.upb.summer.capstone.ui.generate

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import ro.upb.summer.capstone.domain.GeneratedCard

/**
 * Paste notes, generate structured flashcards with Gemini, preview them, then
 * save them as a new deck.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateScreen(
    onBack: () -> Unit,
    onSaved: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenerateViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val saving by viewModel.saving.collectAsStateWithLifecycle()
    var notes by rememberSaveable { mutableStateOf("") }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var showSaveDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri -> if (uri != null) imageUri = uri }

    val loading = state is GenerationState.Loading

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New deck from notes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Paste your notes") },
                minLines = 5,
                enabled = !loading,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedButton(
                onClick = {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
                enabled = !loading,
                modifier = Modifier.padding(top = 12.dp),
            ) {
                Icon(Icons.Outlined.Image, contentDescription = null)
                Text(
                    text = if (imageUri == null) "Attach image" else "Change image",
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            imageUri?.let { uri ->
                ImagePreview(
                    uri = uri,
                    onRemove = { imageUri = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                )
            }

            Button(
                onClick = { viewModel.generate(notes, imageUri) },
                enabled = (notes.isNotBlank() || imageUri != null) && !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                Text(
                    text = "Generate flashcards",
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            when (val s = state) {
                GenerationState.Idle -> Unit

                GenerationState.Loading -> LoadingRow(
                    modifier = Modifier.padding(top = 24.dp),
                )

                is GenerationState.Complete -> GeneratedCards(
                    cards = s.cards,
                    saving = saving,
                    onSaveClick = { showSaveDialog = true },
                    modifier = Modifier.padding(top = 20.dp),
                )

                is GenerationState.Error -> ErrorBanner(
                    message = s.message,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                )
            }
        }
    }

    if (showSaveDialog) {
        SaveDeckDialog(
            onConfirm = { title ->
                showSaveDialog = false
                viewModel.save(title, onSaved)
            },
            onDismiss = { showSaveDialog = false },
        )
    }
}

@Composable
private fun ImagePreview(
    uri: Uri,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = uri,
            contentDescription = "Attached image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp)),
        )
        FilledIconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Remove image")
        }
    }
}

@Composable
private fun LoadingRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = "Generating flashcards…",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
private fun GeneratedCards(
    cards: List<GeneratedCard>,
    saving: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "${cards.size} flashcards",
            style = MaterialTheme.typography.titleMedium,
        )
        cards.forEach { card ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = card.question,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = card.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Button(
            onClick = onSaveClick,
            enabled = !saving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            if (saving) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                Text("Save deck")
            }
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = MaterialTheme.shapes.large,
        modifier = modifier,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun SaveDeckDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save deck") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Deck name") },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title) },
                enabled = title.isNotBlank(),
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}