package com.app.emptyapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Spinner", "Radio", "ListView", "Button")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> SpinnerCalculator()
            1 -> RadioCalculator()
            2 -> ListViewCalculator()
            3 -> ButtonCalculator()
        }
    }
}

// Tab 1: Spinner (Dropdown)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpinnerCalculator() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOp by remember { mutableStateOf("+") }
    var result by remember { mutableStateOf("") }
    val operations = listOf("+", "-", "×", "÷", "%", "//", "^", "√")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Máy Tính - Spinner", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Số thứ nhất") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOp,
                onValueChange = {},
                readOnly = true,
                label = { Text("Phép tính") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                operations.forEach { op ->
                    DropdownMenuItem(
                        text = { Text(op) },
                        onClick = {
                            selectedOp = op
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Số thứ hai") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                result = calculate(num1, num2, selectedOp)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tính toán", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            Text("Kết quả: $result", fontSize = 20.sp)
        }
    }
}

// Tab 2: Radio Button
@Composable
fun RadioCalculator() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var selectedOp by remember { mutableStateOf("+") }
    var result by remember { mutableStateOf("") }
    val operations = listOf("+" to "Cộng", "-" to "Trừ", "×" to "Nhân", "÷" to "Chia",
        "%" to "Chia lấy dư",
        "//" to "Chia nguyên",
        "^" to "Lũy thừa",
        "√" to "Căn bậc")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Máy Tính - Radio Button", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Số thứ nhất") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Chọn phép tính:", fontSize = 16.sp)
        operations.forEach { (op, label) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOp == op,
                    onClick = { selectedOp = op }
                )
                Text(text = "$label ($op)", modifier = Modifier.padding(start = 8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Số thứ hai") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                result = calculate(num1, num2, selectedOp)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tính toán", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            Text("Kết quả: $result", fontSize = 20.sp)
        }
    }
}

// Tab 3: ListView với Snackbar
@Composable
fun ListViewCalculator() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var selectedOp by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val operations = listOf(
        "+" to "Cộng",
        "-" to "Trừ",
        "×" to "Nhân",
        "÷" to "Chia",
        "%" to "Chia lấy dư",
        "//" to "Chia nguyên",
        "^" to "Lũy thừa",
        "√" to "Căn bậc"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Máy Tính - ListView", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = num1,
                onValueChange = { num1 = it },
                label = { Text("Số thứ nhất") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Chọn phép tính từ danh sách:", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LazyColumn {
                    items(operations) { (op, label) ->
                        Surface(
                            onClick = {
                                selectedOp = op
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Đã chọn: $label ($op)",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            color = if (selectedOp == op)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "$label ($op)",
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = num2,
                onValueChange = { num2 = it },
                label = { Text("Số thứ hai") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedOp != null) {
                        result = calculate(num1, num2, selectedOp!!)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedOp != null
            ) {
                Text("Tính toán", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (result.isNotEmpty()) {
                Text("Kết quả: $result", fontSize = 20.sp)
            }
        }
    }
}

// Tab 4: Button Input
@Composable
fun ButtonCalculator() {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Máy Tính - Button", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Số thứ nhất") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Số thứ hai") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("Chọn phép tính:", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { result = calculate(num1, num2, "+") }) {
                Text("+", fontSize = 24.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "-") }) {
                Text("-", fontSize = 24.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "×") }) {
                Text("×", fontSize = 24.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "÷") }) {
                Text("÷", fontSize = 24.sp)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { result = calculate(num1, num2, "%") }) {
                Text("%", fontSize = 24.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "//") }) {
                Text("//", fontSize = 20.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "^") }) {
                Text("^", fontSize = 24.sp)
            }
            Button(onClick = { result = calculate(num1, num2, "√") }) {
                Text("√", fontSize = 24.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Kết quả: $result",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

fun calculate(num1Str: String, num2Str: String, operation: String): String {
    return try {
        val num1 = num1Str.toDouble()
        val num2 = num2Str.toDouble()

        val result = when (operation) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> {
                if (num2 == 0.0) return "Lỗi: Không thể chia cho 0"
                num1 / num2
            }
            "%" -> {
                if (num2 == 0.0) return "Lỗi: Không thể chia cho 0"
                num1 % num2
            }
            "//" -> {
                if (num2 == 0.0) return "Lỗi: Không thể chia cho 0"
                (num1 / num2).toInt().toDouble()
            }
            "^" -> num1.pow(num2)
            "√" -> {
                if (num1 < 0.0) return "Lỗi: Không thể lấy căn của số âm"
                num1.pow(1.0 / num2)
            }
            else -> return "Phép tính không hợp lệ"
        }

        // Format kết quả
        if (result % 1.0 == 0.0) result.toInt().toString()
        else String.format("%.2f", result)

    } catch (e: NumberFormatException) {
        "Lỗi: Vui lòng nhập số hợp lệ"
    }
}