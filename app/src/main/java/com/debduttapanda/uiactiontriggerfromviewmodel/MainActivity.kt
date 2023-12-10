package com.debduttapanda.uiactiontriggerfromviewmodel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.debduttapanda.uiactiontriggerfromviewmodel.ui.theme.UIActionTriggerFromViewModelTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIActionTriggerFromViewModelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPage()
                }
            }
        }
    }
}



@Composable
fun MyPage(vm: MyViewModel = viewModel()){
    HandleTrigger(eventState = vm.trigger.state){ c, d->
        when(d){
            is ToastEvent->{
                Toast.makeText(c, d.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(){
        Text("Hello")
    }
}




class MyViewModel: ViewModel(){
    val trigger = UITrigger()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            viewModelScope.launch(Dispatchers.IO) {
                trigger.post(ToastEvent("MyToast"))
                trigger.post(ToastEvent("MyToast1"))
                trigger.post(ToastEvent("MyToast2"))
                trigger.post(ToastEvent("MyToast3"))
            }
        }
    }
}

class ToastEvent(
    val message: String,
    shortTime: Boolean = true
): UIEvent()