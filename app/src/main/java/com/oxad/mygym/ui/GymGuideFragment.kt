package com.oxad.mygym.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.oxad.mygym.R
import com.oxad.mygym.model.Workout
import com.oxad.mygym.viewmodel.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GymGuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GymGuideFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val mainViewModel: MainViewModel by activityViewModels()

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
        return ComposeView(requireContext()).apply {
            setContent {
               WorkoutScreen(mainViewModel)
            }
        }
    }
    @Composable
    fun WorkoutScreen(  viewModelIs:MainViewModel ) {
        var isExpanded by remember { mutableStateOf(false) }


        val workouts by viewModelIs.workouts.collectAsState()
        val isLoading by viewModelIs.isLoading.collectAsState()
        val workoutList by remember { mutableStateOf(workouts) }
        Log.e("WorkoutList", "AAA $viewModelIs")
        var selectedMuscleGroup by remember { mutableStateOf("Chest") }

        LaunchedEffect(selectedMuscleGroup) {
            viewModelIs.fetchWorkouts(selectedMuscleGroup)

        }


        Column(modifier = Modifier
            .fillMaxSize()
            .padding( top = 40.dp)) {

            //   Text(text = "Select Muscle Group", style = MaterialTheme.typography.headlineSmall)

            DropdownMenuExample(selectedMuscleGroup) { selectedGroup ->
                selectedMuscleGroup = selectedGroup
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {

                WorkoutList( workouts)

            }
        }
    }

    @Composable
    fun DropdownMenuExample(selectedGroup: String, onGroupSelected: (String) -> Unit) {

        val muscleGroups = listOf("Chest", "Back", "Shoulders","Biceps","Triceps","Forearms", "Legs", "Core", "Full Body")
        val context = LocalContext.current


        Log.e("sdafaf", "Yes enter $selectedGroup")
        var expanded by remember { mutableStateOf(false) }
        LazyRow() {
            items(muscleGroups) { workoutName ->

                val animatedColor by animateColorAsState(
                    targetValue = if (selectedGroup == workoutName) Color(context.getColor(R.color.blue_700)) else Color.DarkGray,
                    animationSpec = tween(durationMillis = 250)
                )

                Button(
                    onClick = {
                        onGroupSelected(workoutName)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = animatedColor)
                    ,modifier = Modifier.fillMaxWidth().padding(10.dp).width(130.dp)


                ) {
                    Text(workoutName)
                }


            }
            /*  Box(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { expanded = true }) {
                Text(selectedGroup)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                muscleGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group) },
                        onClick = {
                            onGroupSelected(group)
                            expanded = false
                        }
                    )
                }
            }
        }*/
        }
    }

    @Composable
    fun WorkoutList(workouts: List<Workout>) {
        var checkForExpand by remember { mutableStateOf(workouts) }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(workouts) { workout ->
                WorkoutItem(workout){

                    checkForExpand = it

                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun WorkoutItem(workout: Workout,onClickedCard: (List<Workout>) -> Unit) {
        var isExpanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    //   workouts.find { it.first.name == workout.first.name }.second
                    // isExpandedIs = !isExpandedIs
                    isExpanded = !isExpanded
                    //onClickedCard(workouts)
                }
                .padding(8.dp),

            // Toggle expanded state on click


            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            if (isExpanded) {

                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
                        Text(text = workout.name,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 12.sp) )// Make text bold
                        Spacer(modifier = Modifier.height(16.dp))
                        Row (modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween, // or Arrangement.SpaceBetween
                            verticalAlignment = Alignment.CenterVertically ){
                            Column {
                                Text(
                                    text = "Category: ${workout.category}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Muscle Group: ${workout.muscle_group}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Difficulty: ${workout.difficulty}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Duration: ${workout.duration} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(70.dp) // Set the size of the image
                                    .clip(CircleShape)
                            ) {

                               GlideImage(
                                    // model = R.drawable.pups,
                                    model = "https://hips.hearstapps.com/hmg-prod/images/mh0418-fit-pul-01-1558554157.jpg?crop=0.750xw:1.00xh;0.250xw,0&resize=500:*",
                                    contentDescription = "Image 1",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            /*    Image(
                                    painter = painterResource(id = R.drawable.pups),
                                    contentDescription = "Image of ${workout.name}",
                                    contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                        .fillMaxSize()
                                    //
                                )*/
                            }
                    }




                }
            }else{
                Text(text = workout.name,
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold) )
            }


        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val workout =
            Workout("Pull ups", "Body Workout", "Uses body for resistance", "All", "Hard", 25)
            // WorkoutScreen(mainViewModel)
               WorkoutItem(workout) {
               }

    }
}


