package com.xtremepixel.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xtremepixel.jettipapp.components.InputField
import com.xtremepixel.jettipapp.ui.theme.JetTipAppTheme
import com.xtremepixel.jettipapp.utils.calculateTotalPerPerson
import com.xtremepixel.jettipapp.utils.calculateTotalTip
import com.xtremepixel.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                // TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }

}

//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 20.0) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .height(150.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp))),
        color = Color.Cyan
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h6)
            Text(
                text = "$$total", style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    val splitNumber = remember {
        mutableStateOf(1)
    }
    val tipAmount = remember {
        mutableStateOf(0.0)
    }

    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }
    BillForm(
        splitNumber = splitNumber,
        tipAmount = tipAmount,
        totalPerPerson = totalPerPerson
    ) {}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitNumber: MutableState<Int>,
    tipAmount: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    onValueChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column() {

        TopHeader(totalPerPerson = totalPerPerson.value)

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {

            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (validState.not()) return@KeyboardActions
                        onValueChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    })
                if (validState) {
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split", modifier = Modifier.align(
                                alignment = Alignment.CenterVertically
                            )
                        )
                        Spacer(modifier = Modifier.width(120.dp))
                        Row(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    if (
                                        splitNumber.value <= 1
                                    ) return@RoundIconButton
                                    splitNumber.value -= 1
                                    totalPerPerson.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitNumber.value,
                                        tipPercentage = tipPercentage
                                    )
                                })
                            Text(
                                text = splitNumber.value.toString(),
                                modifier = modifier
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(start = 8.dp, end = 8.dp)
                            )
                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                    splitNumber.value += 1
                                    totalPerPerson.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        splitBy = splitNumber.value,
                                        tipPercentage = tipPercentage
                                    )
                                })
                        }
                    }
                    // Tip Row
                    Row(
                        modifier = Modifier.padding(start = 3.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Tip", modifier = Modifier.align(
                                alignment = Alignment.CenterVertically
                            )
                        )
                        Spacer(modifier = Modifier.width(200.dp))
                        Text(
                            text = "$${tipAmount.value}",
                            modifier = modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 8.dp, end = 8.dp)
                        )

                    }
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage %")
                        Spacer(modifier = modifier.height(16.dp))
                        Slider(
                            value = sliderPositionState.value,
                            onValueChange = { newValue ->
                                sliderPositionState.value = newValue
                                tipAmount.value = calculateTotalTip(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage
                                )
                                totalPerPerson.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitNumber.value,
                                    tipPercentage = tipPercentage
                                )
                            }, modifier = modifier.padding(start = 16.dp, end = 16.dp),
                            steps = 5
                        )
                    }
                } else {
                    Box() {

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            TopHeader()
        }
    }
}