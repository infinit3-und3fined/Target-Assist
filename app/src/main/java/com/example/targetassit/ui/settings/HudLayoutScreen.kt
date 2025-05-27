package com.example.targetassit.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HudLayoutScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HUD Layout") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game preview area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1A2C38))
                ) {
                    // Simulated game background
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Game Preview",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 16.sp
                        )
                    }
                    
                    // Fixed crosshair in center
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .align(Alignment.Center)
                                .background(Color.White)
                        )
                    }
                    
                    // Draggable fire button
                    DraggableHudElement(
                        initialX = 250f,
                        initialY = 150f,
                        color = Color.Red.copy(alpha = 0.7f),
                        size = 60.dp,
                        label = "FIRE"
                    )
                    
                    // Draggable aim button
                    DraggableHudElement(
                        initialX = 50f,
                        initialY = 150f,
                        color = Color.Blue.copy(alpha = 0.7f),
                        size = 60.dp,
                        label = "AIM"
                    )
                    
                    // Draggable movement joystick
                    DraggableHudElement(
                        initialX = 70f,
                        initialY = 70f,
                        color = Color.Gray.copy(alpha = 0.7f),
                        size = 80.dp,
                        label = "MOVE"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                "Drag elements to customize your HUD layout",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // HUD element customization options
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Button Size",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    
                    Slider(
                        value = 0.7f,
                        onValueChange = { /* Update button size */ },
                        valueRange = 0.5f..1.0f
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        "Button Opacity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    
                    Slider(
                        value = 0.7f,
                        onValueChange = { /* Update opacity */ },
                        valueRange = 0.3f..1.0f
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Save button
            Button(
                onClick = { /* Save layout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Layout")
            }
        }
    }
}

@Composable
fun DraggableHudElement(
    initialX: Float,
    initialY: Float,
    color: Color,
    size: androidx.compose.ui.unit.Dp,
    label: String
) {
    var offsetX by remember { mutableStateOf(initialX) }
    var offsetY by remember { mutableStateOf(initialY) }
    
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(size)
            .clip(CircleShape)
            .background(color)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
} 