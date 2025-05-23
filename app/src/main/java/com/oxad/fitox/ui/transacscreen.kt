package com.oxad.fitox.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oxad.fitox.model.Transaction
import com.oxad.fitox.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginatedTransactionListScreen(  mainVM: MainViewModel) {
    var transactions by remember { mutableStateOf(listOf<Transaction>()) }
    var isLoading by remember { mutableStateOf(true) }
    var page by remember { mutableStateOf(1) }
    val listState = rememberLazyListState()
    // Observe transactions from the ViewModel

    LaunchedEffect(page) {


        //  Log.e("checktloa","loadTransacHistory $page")
//delay(2000)
        mainVM.loadTransacHistory(page)
//Log.e("checkatra","A page: $page")

    }

    LaunchedEffect(Unit) {

        mainVM.transacList.collect { transac ->
            //  Log.e("chejkaf","collect ${Gson().toJson(transac)}")
            // Log.e("chejkaf","Before B ${Gson().toJson(transactions)}")

            transactions = transactions + transac // Append new items
            //  Log.e("chejkaf","collect add ${Gson().toJson(transactions)}")
            if(transactions.isNotEmpty()) {
                isLoading = false
            }
// Update loading state after items are added
            // Log.e("chejkaf","After A ${Gson().toJson(transac)}")
            // Log.e("chejkaf","After B ${Gson().toJson(transactions)}")

        }
    }
    // Detect scrolling to the bottom of the list
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex != null) {
                    Log.e("checktloa","lastindex: : ${transactions.lastIndex} isLoading: $isLoading totalpage: ${mainVM.totalTransacPage}")
//Log.e("checktloa","A $isLoading")
                    if (lastVisibleItemIndex >= transactions.lastIndex && !isLoading && page < mainVM.totalTransacPage) {
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