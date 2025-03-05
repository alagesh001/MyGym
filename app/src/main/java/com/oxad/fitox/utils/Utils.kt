package com.oxad.fitox.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oxad.fitox.R

class Utils {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}
@Composable
fun ConfirmationDialog(
    title: String = "Are you sure?",
    description: String = "This action cannot be undone.",
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var isDialogOpen by remember { mutableStateOf(true) }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false

                               onDismiss()},
           /* title = {
                Text(text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),  fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold

                    )
            },*/
            text = {
                Text(text = description,textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth() .padding(top = 12.dp),  fontSize = 20.sp)
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center // Center the buttons
                ) {
                    TextButton(
                        onClick = {
                            isDialogOpen = false
                            onCancel()
                        },
                        colors = ButtonDefaults.buttonColors(Color(context.getColor(R.color.tertiary_color)))
                    ) {
                        Text(text = cancelText)
                    }
                    Spacer(modifier = Modifier.width(15.dp)) // Add spacing between buttons
                    TextButton(
                        onClick = {
                            isDialogOpen = false
                            onConfirm()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = confirmText, color = Color.White)
                    }
                }
            },
          /*  dismissButton = {
                TextButton(onClick = {
                    isDialogOpen = false
                    onCancel()
                }) {
                    Text(text = cancelText)
                }
            }*/
        )
    }
}