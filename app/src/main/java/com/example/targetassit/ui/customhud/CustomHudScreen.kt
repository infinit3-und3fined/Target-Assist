package com.example.targetassit.ui.customhud

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.targetassit.ui.theme.TargetAssitTheme
import kotlin.math.roundToInt

data class ButtonPosition(
    var x: Float,
    var y: Float,
    val size: Float,
    val label: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHudScreen(
    onBackClick: () -> Unit = {},
    onHudLayoutChange: (List<ButtonPosition>) -> Unit = {}
) {
    // Initial button positions
    val buttons = remember {
        mutableStateOf(
            listOf(
                ButtonPosition(100f, 300f, 60f, "Fire", Color(0xFFE53935).copy(alpha = 0.7f)),
                ButtonPosition(200f, 400f, 50f, "Jump", Color(0xFF43A047).copy(alpha = 0.7f)),
                ButtonPosition(300f, 350f, 55f, "Crouch", Color(0xFF1E88E5).copy(alpha = 0.7f)),
                ButtonPosition(400f, 450f, 65f, "Reload", Color(0xFFFFB300).copy(alpha = 0.7f))
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom HUD Layout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(padding)
        ) {
            Text(
                text = "Customize Button Layout",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
            
            Text(
                text = "Drag buttons to adjust their position",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // HUD preview area with phone frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .aspectRatio(16f / 9f) // Landscape aspect ratio
                    .background(Color(0xFF1E1E1E))
                    .border(2.dp, Color.DarkGray, RoundedCornerShape(8.dp))
            ) {
                // Draw crosshair in center
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val crosshairSize = 20f
                    
                    drawLine(
                        color = Color.Red,
                        start = Offset(centerX - crosshairSize, centerY),
                        end = Offset(centerX + crosshairSize, centerY),
                        strokeWidth = 2f
                    )
                    
                    drawLine(
                        color = Color.Red,
                        start = Offset(centerX, centerY - crosshairSize),
                        end = Offset(centerX, centerY + crosshairSize),
                        strokeWidth = 2f
                    )
                }
                
                // Draggable buttons
                buttons.value.forEachIndexed { index, button ->
                    var offsetX by remember { mutableStateOf(button.x) }
                    var offsetY by remember { mutableStateOf(button.y) }
                    
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .size(button.size.dp)
                            .clip(CircleShape)
                            .background(button.color)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                    
                                    // Update the button position in the list
                                    val updatedButtons = buttons.value.toMutableList()
                                    updatedButtons[index] = button.copy(x = offsetX, y = offsetY)
                                    buttons.value = updatedButtons
                                    
                                    // Notify about the change
                                    onHudLayoutChange(buttons.value)
                                }
                            }
                    ) {
                        Text(
                            text = button.label,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Tip: Position buttons where your thumbs naturally rest for optimal control",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun CustomHudScreenPreview() {
    TargetAssitTheme {
        CustomHudScreen()
    }
} 