package com.example.tugas5ppb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tugas5ppb.ui.theme.Tugas5ppbTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tugas5ppbTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LengthConversionScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LengthConversionScreen() {
    var value by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Meter (m)") }

    val units = listOf(
        "Kilometer (km)",
        "Hectometer (hm)",
        "Decameter (dam)",
        "Meter (m)",
        "Decimeter (dm)",
        "Centimeter (cm)",
        "Millimeter (mm)"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Length Conversion", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedUnit,
                onValueChange = {},
                readOnly = true,
                label = { Text("Unit") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            selectedUnit = unit
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Converted Values", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ConversionResults(value, selectedUnit)
    }
}

@Composable
fun ConversionResults(value: String, selectedUnit: String) {
    val input = value.toDoubleOrNull() ?: 0.0
    val factor = when (selectedUnit) {
        "Kilometer (km)" -> 1000.0
        "Hectometer (hm)" -> 100.0
        "Decameter (dam)" -> 10.0
        "Meter (m)" -> 1.0
        "Decimeter (dm)" -> 0.1
        "Centimeter (cm)" -> 0.01
        "Millimeter (mm)" -> 0.001
        else -> 1.0
    }
    val meters = input * factor

    val conversions = listOf(
        "Kilometer (km)" to meters / 1000,
        "Hectometer (hm)" to meters / 100,
        "Decameter (dam)" to meters / 10,
        "Meter (m)" to meters,
        "Decimeter (dm)" to meters * 10,
        "Centimeter (cm)" to meters * 100,
        "Millimeter (mm)" to meters * 1000
    )

    conversions.forEach { (unit, result) ->
        // Format the result: if it's effectively an integer, show no decimals; otherwise, show up to 5 decimal places without trailing zeros
        val formattedResult = if (result == result.toLong().toDouble()) {
            result.toLong().toString() // Show as integer (e.g., 0 or 40)
        } else {
            "%.5f".format(result).trimEnd('0').trimEnd('.') // Show up to 5 decimals, remove trailing zeros and decimal point if unnecessary
        }

        OutlinedTextField(
            value = formattedResult,
            onValueChange = {},
            label = { Text(unit) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLengthConversionScreen() {
    Tugas5ppbTheme {
        LengthConversionScreen()
    }
}