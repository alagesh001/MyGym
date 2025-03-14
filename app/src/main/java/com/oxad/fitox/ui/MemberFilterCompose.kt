package com.oxad.fitox.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.oxad.fitox.R
import com.oxad.fitox.database.ApiRepository
import com.oxad.fitox.model.Member
import com.oxad.fitox.model.TrainerModel
import com.oxad.fitox.model.Transaction
import com.oxad.fitox.utils.DarkBackgroundView
import com.oxad.fitox.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemberFilterCompose.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberFilterCompose : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private var gymId:Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainViewModel.totalTransacPage = 1

        return ComposeView(requireContext()).apply {
            setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White) // Set background color
                ) {
                   /* AndroidView(
                        factory = { context -> DarkBackgroundView(context, null) },
                        modifier = Modifier.matchParentSize()
                    )*/
                    gymId = mainViewModel.gymResult[mainViewModel.currentGymSelected].GymId
                    when (arguments?.getString("type_is")) {
                        "gender" -> MemberScreen()
                        "trainer" -> TrainerListScreen()
                        "age" -> AgeGroupedMemberListScreen()
                        "dob" -> UpcomingBirthdaysScreen()
                        "goal" -> GoalListScreen()
                        "transac" -> PaginatedTransactionListScreen()

                    }
                }

            }
        }
    }
    @Composable
    fun GoalListScreen() {
        val scope = rememberCoroutineScope()
        var goalList by remember { mutableStateOf<List<Member>>(emptyList()) }

        LaunchedEffect(Unit) {
            scope.launch {
                goalList = mainViewModel.getTotalMemberList()// Group members by trainer ID

            }
        }
        val groupedMembers = goalList.groupBy { it.goal } // Group members by trainer ID
        var expandedGoal by remember { mutableStateOf<Int?>(null) } // Track expanded trainer
        LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
            groupedMembers.forEach { (goal, goalMembers) ->
                val goalName =
                    when(goal){
1 -> "Weight Loss"
    2 -> "Weight Gain"
        3 -> "Muscle Tone"
            4 -> "Others"
                        else -> {"Others"}
                    }
               // val trainerName = trainerList.find { it.coachid == trainerId }?.name ?: "Unknown Trainer" // Get trainer name
                // 🔹 Expandable Trainer Section
                item {
                    ExpandableSection(
                        title = goalName,

                        isExpanded = expandedGoal == goal,
                        count = goalMembers.size, // Pass count here
                        onClick = {
                            expandedGoal = if (expandedGoal == goal) null else goal
                        }
                    )

                }

                // 🔹 Show Members Under Trainer When Expanded
                if (expandedGoal == goal) {
                    items(goalMembers) { member ->
                            MemberItem(member)


                    }
                }
            }
        }
    }
    @Composable
    fun UpcomingBirthdaysScreen() {
        val scope = rememberCoroutineScope()
        var memberList by remember { mutableStateOf<List<Member>>(emptyList()) }
val context = LocalContext.current
        // 🔹 Fetch Members Once
        LaunchedEffect(Unit) {
            scope.launch {
                memberList = mainViewModel.getTotalMemberList()
            }
        }

        // 🔹 Sort Members by Upcoming Birthday
        val sortedMembers = memberList.sortedBy { getDaysUntilNextBirthday(it.dob) }

        LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
            item {
                Text(
                    text = "Upcoming Birthdays 🎂",
                    color = colorResource(id = R.color.primary_color),

                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(sortedMembers) { member ->
                BirthdayItem(member)
            }
        }
    }

    // 🔹 UI for Birthday Item
    @Composable
    fun BirthdayItem(member: Member) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(context.getColor(R.color.tertiary_color))), // Light Pink
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    color = Color.White,
                    text = member.name,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text("🎉 Birthday: ${formatDate(member.dob)}",  color = Color.White,)
                Text("📅 Days Left: ${getDaysUntilNextBirthday(member.dob)}",  color = Color.White,)
            }
        }
    }

    // 🔹 Function to Get Days Until Next Birthday
    fun getDaysUntilNextBirthday(dob: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birthDate = LocalDate.parse(dob, formatter)
        val today = LocalDate.now()

        var nextBirthday = birthDate.withYear(today.year) // Set this year's birthday

        if (nextBirthday.isBefore(today) ) {
            nextBirthday = nextBirthday.plusYears(1) // Move to next year if passed
        }

        return ChronoUnit.DAYS.between(today, nextBirthday)
    }

    // 🔹 Format Date for Display
    fun formatDate(dob: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val birthDate = LocalDate.parse(dob, formatter)
        return birthDate.format(DateTimeFormatter.ofPattern("MMM dd"))
    }

    @Composable
    fun AgeGroupedMemberListScreen() {
        val scope = rememberCoroutineScope()
        var memberList by remember { mutableStateOf<List<Member>>(emptyList()) }

        // 🔹 Fetch Members Once
        LaunchedEffect(Unit) {
            scope.launch {
                memberList = mainViewModel.getTotalMemberList()
            }
        }

        // 🔹 Group members by age category
        val groupedMembers = memberList.groupBy { getAgeGroup(it.dob) }
        var expandedGroup by remember { mutableStateOf<String?>(null) } // Track expanded section

        LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
            groupedMembers.forEach { (ageGroup, members) ->
                // 🔹 Expandable Age Group Section
                item {
                    ExpandableSection(
                    title = ageGroup,
                    isExpanded = expandedGroup == ageGroup,
                    count = members.size, // Pass count here
                    onClick = {
                        expandedGroup = if (expandedGroup == ageGroup) null else ageGroup
                    }
                )

                }

                // 🔹 Show Members When Expanded
                if (expandedGroup == ageGroup) {
                    items(members) { member ->
                            MemberItem(member)


                    }
                }
            }
        }
    }

    // 🔹 Function to Determine Age Group from DOB
    fun getAgeGroup(dob: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Adjust format if needed
        val birthDate = LocalDate.parse(dob, formatter)
        val age = Period.between(birthDate, LocalDate.now()).years

        return when {
            age < 20 -> "< 20"
            age in 20..29 -> "20 - 30"
            age in 30..39 -> "30 - 40"
            age in 40..49 -> "40 - 50"
            else -> "50+"
        }
    }
    @Composable
    fun TrainerListScreen() {
        val scope = rememberCoroutineScope()
        var trainerList by remember { mutableStateOf<List<TrainerModel>>(emptyList()) }
        var memberListIs by remember { mutableStateOf<List<Member>>(emptyList()) }


        LaunchedEffect(Unit) {
            scope.launch {
                trainerList = mainViewModel.getTrainerList()// Group members by trainer ID
                memberListIs = mainViewModel.getTotalMemberList()// Group members by trainer ID

            }
        }
        val groupedMembers = memberListIs.groupBy { it.coachid }

// 🔹 Separate Unknown Trainers
        val knownTrainers = groupedMembers.filterKeys { trainerId ->
            trainerList.any { it.coachid == trainerId }
        }
        val unknownTrainers = groupedMembers.filterKeys { trainerId ->
            trainerList.none { it.coachid == trainerId }
        }

        var expandedTrainer by remember { mutableStateOf<Long?>(null) } // Track expanded trainer

        LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
            // 🔹 Loop through Known Trainers
            knownTrainers.forEach { (trainerId, trainerMembers) ->
                val trainerName = trainerList.find { it.coachid == trainerId }?.name ?: "Unknown Trainer"

                item {
                    ExpandableSection(
                        title = trainerName,
                        isExpanded = expandedTrainer == trainerId,
                        count = trainerMembers.size,
                        onClick = {
                            expandedTrainer = if (expandedTrainer == trainerId) null else trainerId
                        }
                    )
                }

                if (expandedTrainer == trainerId) {
                    items(trainerMembers) { member ->
                            MemberItem(member)


                    }
                }
            }

            // 🔹 Handle "Unknown Trainer" Group
            if (unknownTrainers.isNotEmpty()) {
                item {
                    ExpandableSection(
                        title = "Unknown Trainer",
                        isExpanded = expandedTrainer == -1L, // Using -1 for Unknown Trainer
                        count = unknownTrainers.values.sumOf { it.size }, // Count all unknowns
                        onClick = {
                            expandedTrainer = if (expandedTrainer == -1L) null else -1L
                        }
                    )
                }

                if (expandedTrainer == -1L) {
                    unknownTrainers.values.flatten().forEach { member ->
                        item {

                                MemberItem(member)


                        }
                    }
                }
            }
        }
    }
    @Composable
    fun MemberScreen() {
        val scope = rememberCoroutineScope()
        var memberListIs by remember { mutableStateOf<List<Member>>(emptyList()) }

        LaunchedEffect(Unit) {
            scope.launch {
                memberListIs = mainViewModel.getTotalMemberList()// Group members by trainer ID

            }
        }
        val groupedMembers = memberListIs.groupBy { it.gender } // Group members by trainer ID
        var expandedGender by remember { mutableStateOf<String?>(null) } // Track expanded trainer
        LazyColumn(modifier = Modifier.padding(top = 50.dp)) {
            groupedMembers.forEach { (gender, genderMembers) ->
                // 🔹 Expandable Trainer Section
                item {
                    ExpandableSection(
                        title = gender?:"Others",
                        isExpanded = expandedGender == gender,
                        count = genderMembers.size, // Pass count here
                        onClick = {
                            expandedGender = if (expandedGender == gender) null else gender
                        }
                    )

                }

                // 🔹 Show Members Under Trainer When Expanded
                if (expandedGender == gender) {
                    items(genderMembers) { member ->
                            MemberItem(member)
                    }
                }
            }
        }
    }
    @Composable
    fun ExpandableSection(title: String, isExpanded: Boolean, count: Int, onClick: () -> Unit) {
      val context = LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(containerColor =Color(context.getColor(R.color.tertiary_color))), // Light Blue
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🔹 Title
                Text(
                    text = title,
                    color = Color.White,

                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f) // Takes available space
                )

                // 🔹 Rounded Member Count
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp) // Size of the circle
                        .clip(CircleShape) // Makes it round
                        .background(color = Color(context.getColor(R.color.primary_color))) // Orange color
                ) {
                    Text(
                        text = count.toString(),
                        color =Color(context.getColor(R.color.fourth_color)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
    @Composable
    fun <T> T.AnimationBox(
        enter: EnterTransition = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(500)),
        exit: ExitTransition = fadeOut(animationSpec = tween(500)) + shrinkVertically(animationSpec = tween(500)),
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
    @Composable
    fun MemberItem(member: Member) {
        var expanded by remember { mutableStateOf(false) } // Track expansion state
        val  context = LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = keyframes {
                    durationMillis = if (expanded) 70 else 50
                }) { initialValue, targetValue ->
                }
                .padding(8.dp),

            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.secondary_color),
            ) ,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)         ) {
            Column{
                // Clickable title to expand/collapse details
                Text(
                    text = member.name,
                    fontSize = 18.sp,
color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                        .padding(12.dp)
                )

                if (expanded) {
                    Column(modifier = Modifier.padding(16.dp)
                       ) {
                        Text(
                            color = Color.White,
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Expiry: ")
                                }
                                append("${member.renewal_date}")
                            }

                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Space between items
                        Text(
                            color = Color.White,

                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Mobile: ")
                                }
                                append("${member.mobile}")
                            }

                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Space between items
                        Text(
                            color = Color.White,

                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Height: ")
                                }
                                append("${member.height} cm")
                            }

                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Space between items
                        Text(
                            color = Color.White,

                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Weight: ")
                                }
                                append("${member.weight} kg")
                            }

                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Space between items
                    }


                }
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaginatedTransactionListScreen() {
        var transactions by remember { mutableStateOf(listOf<Transaction>()) }
        var isLoading by remember { mutableStateOf(false) }
        var page by remember { mutableStateOf(1) }
        val listState = rememberLazyListState()
        // Observe transactions from the ViewModel

        LaunchedEffect(page) {


          //  Log.e("checktloa","loadTransacHistory $page")

            mainViewModel.loadTransacHistory(page)
//Log.e("checkatra","A page: $page")

        }
        LaunchedEffect(Unit) {
            mainViewModel.transacList.collect { transac ->
              //  Log.e("chejkaf","collect ${Gson().toJson(transac)}")
                // Log.e("chejkaf","Before B ${Gson().toJson(transactions)}")

                transactions = transactions + transac // Append new items
              //  Log.e("chejkaf","collect add ${Gson().toJson(transactions)}")

                isLoading = false // Update loading state after items are added
                // Log.e("chejkaf","After A ${Gson().toJson(transac)}")
                // Log.e("chejkaf","After B ${Gson().toJson(transactions)}")

            }
        }
        // Detect scrolling to the bottom of the list
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex != null) {
                        Log.e("checktloa","lastindex: : ${transactions.lastIndex} isLoading: $isLoading totalpage: ${mainViewModel.totalTransacPage}")
//Log.e("checktloa","A $isLoading")
                        if (lastVisibleItemIndex >= transactions.lastIndex && !isLoading && page < mainViewModel.totalTransacPage) {
                         //   Log.e("checktloa","B $isLoading")
                            isLoading = true
                            page++
                          //  Log.e("checktloa","page added $page")

                        }
                    }
                }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Transaction History") }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
                item {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun TransactionItem(transaction: Transaction) {
        val typeIs = when(transaction.type){
            0 -> "Delete"
            1 -> "New"
            2 -> "Renew"
            3 -> "Due Pay"
            else -> "Unknown"
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "+ ₹${transaction.amount}", fontWeight = FontWeight.Bold)
                Text(text = "Name: ${transaction.name}")
                Text(text = "Date: ${transaction.date}")
                Text(text = "Transaction Type: $typeIs")
            }
        }
    }


    @Composable
    fun PreviewPaginatedTransactionList() {
        MaterialTheme {
            PaginatedTransactionListScreen()
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun PreviewProfileScreen() {
        // ProfileScreen()
     //   PreviewPaginatedTransactionList()
        //ChangePasswordDialog(){


    }
}


