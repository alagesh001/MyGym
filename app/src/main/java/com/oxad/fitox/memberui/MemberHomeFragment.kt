package com.oxad.fitox.memberui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.oxad.fitox.R
import com.oxad.fitox.databinding.FragmentMemberHomeBinding
import com.oxad.fitox.databinding.FragmentTransacBinding
import com.oxad.fitox.ui.PaginatedTransactionListScreen

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemberHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMemberHomeBinding

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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMemberHomeBinding.inflate(inflater, container, false)
        return binding.root    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = binding.composeView
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        composeView.setContent {
            GymAppUI()
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun GymAppUI() {
        val navController = rememberNavController()
        val activeTab = rememberSaveable { mutableStateOf("Profile") }
       // Text("Hello from Compose") // replace this with your real UI to test

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Fitox Member") },
                    actions = {
                        Button(
                            onClick = {    },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Logout", color = Color.White)
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(activeTab, navController)
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                when (activeTab.value) {
                    "Me" -> ProfileScreen()
                    "Dietician" -> ProfileScreen()
                    "Mark" -> ProfileScreen()
                    "Cart" -> ProfileScreen()
                    "Workout" -> ProfileScreen()
                }
            }
        }
    }
    @Composable
    fun BottomNavigationBar(activeTab: MutableState<String>, navController: NavHostController) {
        val tabs = listOf("Mark", "Cart", "Dietician", "Workout", "Me")

        NavigationBar(containerColor = Color.White) {
            tabs.forEach { tab ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = when (tab) {
                                "Mark" -> Icons.Default.Check
                                "Cart" -> Icons.Default.ShoppingCart
                                "Dietician" -> Icons.Default.Person
                                "Workout" -> Icons.Default.FitnessCenter
                                "Me" -> Icons.Default.Home
                                else -> Icons.Default.Home
                            },
                            contentDescription = tab
                        )
                    },
                    label = { Text(tab) },
                    selected = activeTab.value == tab,
                    onClick = {
                        activeTab.value = tab
                        // Navigate using navController if needed:
                        // navController.navigate(tab.lowercase()) { popUpTo("root") { inclusive = true } }
                    }
                )
            }
        }
    }
    data class User(
        val fullName: String,
        val userId: String,
        val rDate: String,
        val logo: String
    )
    @Composable
    fun ProfileScreen() {
        var editing by remember { mutableStateOf(false) }
        var oldPass by remember { mutableStateOf("") }
        var newPass by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        val mockUserData = User(
            fullName = "John Doe",
            userId = "MEM123456",
            rDate = "2025-12-31",
            logo = "https://example.com/profile.jpg"
        )
        val user = remember { mockUserData } // replace with your state

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Member Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberImagePainter(user.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileField("Full Name", user.fullName)
            ProfileField("Member ID", user.userId)
            ProfileField("Expiry Date", user.rDate)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Password", fontWeight = FontWeight.Medium)

            if (editing) {

                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Old Password") }
                    )
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = { Text("New Password") }
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { /* save logic */ }) {
                            Text("Save")
                        }
                        OutlinedButton(onClick = {
                            editing = false
                            oldPass = ""
                            newPass = ""
                            message = ""
                        }) {
                            Text("Cancel")
                        }
                    }
                }

            } else {
                Column {
                    Text("•••••••••")

                    TextButton(onClick = { editing = true }) {
                        Text("✏️ Edit")
                    }
                }
            }
        }
            if (message.isNotBlank()) {
                Text(message, color = Color.Red)
            }
        }
    }

    @Composable
    fun ProfileField(label: String, value: String) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontWeight = FontWeight.Medium)
            Text(value)
        }
        Spacer(Modifier.height(8.dp))
    }

}