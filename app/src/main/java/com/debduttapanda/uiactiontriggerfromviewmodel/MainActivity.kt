package com.debduttapanda.uiactiontriggerfromviewmodel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
                    val vm: MyViewModel = viewModel()
                    Column(){
                        Button(onClick = {
                            vm.onButtonClick()
                        }) {
                            Text("Call Api")
                        }
                    }
                    HandleTrigger(eventState = vm.trigger.state){context, event->
                        when(event){
                            is ToastEvent->{
                                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }



}

class MyViewModel: ViewModel(){
    val trigger = UITrigger()
    private suspend fun reallyCallApi(): Boolean {
        delay(3000)
        return false
    }
    fun onButtonClick(){
        viewModelScope.launch {
            val result = reallyCallApi()
            if(!result){
                showToast()
            }
        }
    }

    private fun showToast() {
        trigger.post(ToastEvent("Error"))
    }
}

class ToastEvent(val message: String, shortTime: Boolean = true): UIEvent()