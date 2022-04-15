package com.xtremepixel.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xtremepixel.jettipapp.ui.theme.JetTipAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MyApp {
                    TopHeader()
                }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
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

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 20.0){

    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(16.dp))),
    color = Color.Cyan) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                val total = "%.2f".format(totalPerPerson)
                Text(text = "Total Per Person", style = MaterialTheme.typography.h6)
                Text(text = "$$total", style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold)
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