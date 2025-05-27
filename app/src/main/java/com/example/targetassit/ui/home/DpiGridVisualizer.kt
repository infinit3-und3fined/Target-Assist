package com.example.targetassit.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.targetassit.ui.theme.BackgroundDark
import com.example.targetassit.ui.theme.PrimaryBlue
import com.example.targetassit.ui.theme.SecondaryTeal

@Composable
fun DpiGridVisualizer(
    dpi: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Terminal header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = SecondaryTeal,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Target-Assist Terminal",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            // Terminal content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            ) {
                TerminalLine(
                    prefix = "user@target-assist:~$",
                    command = " ls -la",
                    prefixColor = PrimaryBlue
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "total 24",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                TerminalLine(
                    prefix = "drwxr-xr-x",
                    command = " overlay/",
                    prefixColor = SecondaryTeal
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                TerminalLine(
                    prefix = "drwxr-xr-x",
                    command = " config/",
                    prefixColor = SecondaryTeal
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                TerminalLine(
                    prefix = "-rw-r--r--",
                    command = " sensitivity.conf",
                    prefixColor = Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TerminalLine(
                    prefix = "user@target-assist:~$",
                    command = " cd overlay",
                    prefixColor = PrimaryBlue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TerminalLine(
                    prefix = "user@target-assist:~/overlay$",
                    command = " ls",
                    prefixColor = PrimaryBlue
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "crosshair.conf",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = SecondaryTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "target_service.sh",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TerminalLine(
                    prefix = "user@target-assist:~/overlay$",
                    command = " _",
                    prefixColor = PrimaryBlue
                )
            }
        }
    }
}

@Composable
fun TerminalLine(
    prefix: String,
    command: String,
    prefixColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prefix,
            color = prefixColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
        
        Text(
            text = command,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp
        )
    }
} 