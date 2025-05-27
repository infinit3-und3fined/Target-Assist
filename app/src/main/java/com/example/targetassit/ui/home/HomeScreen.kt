package com.example.targetassit.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.targetassit.R
import com.example.targetassit.ui.home.components.CircleButton
import com.example.targetassit.ui.theme.TargetAssitTheme

@Composable
fun HomeScreen(
    onStartOverlayClick: () -> Unit = {},
    onSensitivityClick: () -> Unit = {},
    onCustomHudClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // Background pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.1f)
        ) {
            // We would use a repeating pattern here
            // This is a placeholder for the background pattern
        }

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Title section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.home_title),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = stringResource(R.string.home_subtitle),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Main action button
            CircleButton(
                icon = Icons.Filled.Star,
                label = stringResource(R.string.btn_start_overlay),
                onClick = onStartOverlayClick,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Secondary actions in a horizontal row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleButton(
                    icon = Icons.Filled.Add,
                    label = stringResource(R.string.btn_sensitivity),
                    onClick = onSensitivityClick
                )
                
                CircleButton(
                    icon = Icons.Filled.Edit,
                    label = stringResource(R.string.btn_custom_hud),
                    onClick = onCustomHudClick
                )
                
                CircleButton(
                    icon = Icons.Filled.Settings,
                    label = stringResource(R.string.btn_settings),
                    onClick = onSettingsClick
                )
                
                CircleButton(
                    icon = Icons.Outlined.Info,
                    label = stringResource(R.string.btn_about),
                    onClick = onAboutClick
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun HomeScreenPreview() {
    TargetAssitTheme {
        Surface {
            HomeScreen()
        }
    }
} 