package ro.upb.summer.capstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ro.upb.summer.capstone.ui.AppNavigation
import ro.upb.summer.capstone.ui.theme.CapstoneStarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapstoneStarterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

data class CounterState(
    val count: Int
)

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterState(0))
    val state: StateFlow<CounterState>
        get() = _state

    fun onIncrement() {
        _state.value = CounterState(_state.value.count)
    }
}

@Composable
fun CounterScreen() {
    val viewModel: CounterViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Counter(
        counter = state.count,
        onCountIncrement = { viewModel.onIncrement() }
    )
}

@Composable
fun Counter(counter: Int, onCountIncrement: () -> Unit) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Clicked: $counter times")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onCountIncrement()
            println("Clicked: {$counter.value}")
        }) {
            Text("Click me")
        }
    }
}