package com.oxad.mygym.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.gson.Gson
import com.oxad.mygym.R
import com.oxad.mygym.databinding.FragmentGymGuideBinding
import com.oxad.mygym.databinding.FragmentSearchFilterBinding
import com.oxad.mygym.model.Workout
import com.oxad.mygym.utils.DarkBackgroundView
import com.oxad.mygym.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class GymGuideFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentGymGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGymGuideBinding.inflate(inflater, container, false)
        return binding.root
      /*  return binding.composeView.apply {


        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
if (!viewModel.gymCoachOpenedFirsttime) {
    viewModel.gymCoachOpenedFirsttime = true
    viewLifecycleOwner.lifecycleScope.launch {
        delay(15) // 1-second delay
        val composeView = binding.composeStub.inflate().findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            MyComposeUI()
        }
    }
}else{
    val composeView = binding.composeStub.inflate().findViewById<ComposeView>(R.id.composeView)
    composeView.setContent {
        MyComposeUI()
    }
}
        /*viewLifecycleOwner.lifecycleScope.launch {
            binding.composeView.setContent {

                //MyComposeUI()

            }*/
   //     }

    }
        @Composable
        fun MyComposeUI() {
            val context = LocalContext.current
            val darkBackgroundView = remember { DarkBackgroundView(context, null) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            ) {

                AndroidView(
                    factory = { darkBackgroundView },
                    modifier = Modifier.matchParentSize()
                )

                WorkoutScreen() // Ensure this is optimized too
            }
        }

        @Composable
    fun WorkoutScreen(   ) {
        var isExpanded by remember { mutableStateOf(false) }


        val workouts by viewModel.workouts.collectAsState()
        val isLoading by viewModel.isLoading.collectAsState()
        val workoutList by remember { mutableStateOf(workouts) }
        //Log.e("WorkoutList", "AAA $viewModel")
        var selectedMuscleGroup by remember { mutableStateOf("Chest") }
       // Log.e("jdacjadf","fetchinggg  "+ Gson().toJson(workouts))

        LaunchedEffect(selectedMuscleGroup) {
           // viewModel.fetchWorkouts(selectedMuscleGroup)
            withContext(Dispatchers.IO) {
                viewModel.fetchgymGuide(selectedMuscleGroup)
            }
        }


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)) {

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
                    targetValue = if (selectedGroup == workoutName) Color(context.getColor(R.color.secondary_color)) else Color(context.getColor(R.color.primary_color)),
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

        LazyColumn {
            items(workouts) { workout ->
                    WorkoutItem(workout){

                        checkForExpand = it

                    }

            }
        }
    }
    @Composable
    fun <T> T.AnimationBox(
        enter: EnterTransition = expandVertically(animationSpec = tween(200)) + fadeIn(animationSpec = tween(500)),
        exit: ExitTransition = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(300)),
        content: @Composable T.() -> Unit
    ) {
        val state = remember {
            MutableTransitionState(false).apply {
                // Start the animation immediately.
                targetState = true
            }
        }

        AnimatedVisibility(
            visibleState = state,
            enter = enter,
            exit = exit
        ) { content() }
    }
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun WorkoutItem(workout: Workout,onClickedCard: (List<Workout>) -> Unit) {
        val context = LocalContext.current

        var isExpanded by remember { mutableStateOf(false) }

        Card( colors = CardDefaults.cardColors(
            containerColor = Color(context.getColor(R.color.tertiary_color)),
            contentColor =  Color.White
        ),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize (animationSpec = keyframes {
                    durationMillis = if (isExpanded) 70 else 50
                }){ initialValue, targetValue ->
                }
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
            AnimatedVisibility (isExpanded) {

                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
                        Text(text = workout.name,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 16.sp) )// Make text bold
                        Spacer(modifier = Modifier.height(8.dp))

                        Row (modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween, // or Arrangement.SpaceBetween
                            verticalAlignment = Alignment.CenterVertically ) {
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
                   /* Box(
                        modifier = Modifier
                            .size(70.dp) // Set the size of the image
                            .clip(CircleShape)
                    ) {

                     *//*   GlideImage(
                            // model = R.drawable.pups,
                            model = "https://hips.hearstapps.com/hmg-prod/images/mh0418-fit-pul-01-1558554157.jpg?crop=0.750xw:1.00xh;0.250xw,0&resize=500:*",
                            contentDescription = "Image 1",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )*//*
                        Image(
                            painter = painterResource(id = R.drawable.pups),
                            contentDescription = "Image of ${workout.name}",
                            contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                .fillMaxSize()
                            //
                        )
                    }*/
                        }


                }
            }
            AnimatedVisibility (!isExpanded) {
                Text(text = workout.name,
                    modifier = Modifier.padding(15.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold) )
            }



        }
    }
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
            val workout =  Workout(1,"Pull ups","Body Workout","Uses body for resistance","All","Hard",25)
            // WorkoutItem(workout){
            //WorkoutScreen()
            //WorkoutItem(workout) {



    }

}


