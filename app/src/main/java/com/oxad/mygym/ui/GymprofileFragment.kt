package com.oxad.mygym.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oxad.mygym.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GymprofileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GymprofileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileScreen()
            }
        }    }


    @Composable
    fun ProfileScreen() {
        var name by remember { mutableStateOf("John Doe") }
        var location by remember { mutableStateOf("New York, USA") }
        val plan = "Annual Plan - Expires on March 30, 2026" // Static for now, can be fetched dynamically
        var showChangePasswordDialog by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val primefontcolor =  Color(context.getColor(R.color.primary_color))
        val secfontcolor =  Color(context.getColor(R.color.secondary_color))
        val terfontcolor =  Color(context.getColor(R.color.tertiary_color))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = primefontcolor)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Name:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = primefontcolor)
            Text(text = name, fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp), color = terfontcolor)

            Text(text = "Location:", fontSize = 16.sp, color = primefontcolor, fontWeight = FontWeight.SemiBold,modifier = Modifier.padding(top = 16.dp))
            Text(text = location, fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp), color = terfontcolor)

            Text(text = "Active Plan:", fontSize = 16.sp, color = primefontcolor, fontWeight = FontWeight.SemiBold,modifier = Modifier.padding(top = 16.dp))
            Text(
                text = plan,
                fontSize = 18.sp,
                color =  terfontcolor,
                modifier = Modifier.padding(top = 8.dp,bottom = 24.dp)
            )

            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(context.getColor(R.color.secondary_color)))
            ) {
                Text("Change Password")
            }

            if (showChangePasswordDialog) {
                ChangePasswordDialog(onDismiss = { showChangePasswordDialog = false })
            }
        }
    }

    @Composable
    fun ChangePasswordDialog(onDismiss: () -> Unit) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        val context = LocalContext.current
        val customOutlineColor = Color(0xFF6200EE) // Custom outline color (Purple)
        val textStyle = TextStyle(fontSize = 18.sp) // Increased text size
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Change Password",color = Color(context.getColor(R.color.tertiary_color))) },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Old Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = textStyle,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = customOutlineColor,
                            unfocusedBorderColor = Color(context.getColor(R.color.tertiary_color))
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = textStyle,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = customOutlineColor,
                            unfocusedBorderColor = Color(context.getColor(R.color.secondary_color))
                        )

                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Re-enter New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = textStyle,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = customOutlineColor,
                            unfocusedBorderColor = Color(context.getColor(R.color.secondary_color))
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                    }
                }
            },
            confirmButton = {
                Button(colors = ButtonDefaults.buttonColors(Color(context.getColor(R.color.secondary_color))), onClick = {
                    if (newPassword == confirmPassword) {
                        errorMessage = ""
                        onDismiss()
                    } else {
                        errorMessage = "Passwords do not match"
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color(context.getColor(R.color.tertiary_color)))) {
                    Text("Cancel")
                }
            }
        )
    }


    @Preview(showBackground = true)
    @Composable
    fun PreviewProfileScreen() {
        ProfileScreen()
        //ChangePasswordDialog(){


    }

}