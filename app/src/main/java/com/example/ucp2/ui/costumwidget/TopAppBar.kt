package com.example.ucp2.ui.costumwidget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    onBack: () -> Unit,
    showBackButton: Boolean = true,
    judul: String,
    logo: ImageVector? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF9861FF))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showBackButton) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "Back")
                }
                Spacer(modifier = Modifier.weight(2f))
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            logo?.let {
                Image(
                    imageVector = it,
                    contentDescription = "Logo",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Text(
                text = judul,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
