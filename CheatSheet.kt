package com.mukul.jan.compose.learn

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

@Composable
fun CheatSheet(onTimeOut: () -> Unit){

    //--------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------- State ------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------------

    /**
     * What is state in jetpack compose?
     * State is responsible for redraw the ui (recomposition) when value inside state changes
     *
     */
    val counter = remember { mutableStateOf(1) }
    /**
     * what is remember { } ?
     * It save the state for recomposition
     */
    val counter1 = rememberSaveable { mutableStateOf(1) }
    /**
     * what is rememberSaveable { } ?
     * It save the state for recomposition/orientation changes/ while navigation to another screen,
     * it save the state when you go back to that screen
     */
    val selectedUser = rememberSaveable { mutableStateOf(User("mukul")) }
    /**
     * You must annotate User class with @Parceable
     */
    val navController = rememberNavController()
    /**
     * rememberNavController() used for creating NavController
     */
    val scope = rememberCoroutineScope()
    /**
     * It a CoroutineScope for launching jobs inside composables
     */
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){
        //result
    }
    /**
     * rememberLauncherForActivityResult is replacement for onActivityResult()
     */

    //--------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------- Side Effects -----------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------------

    val currentOnTimeout by rememberUpdatedState(onTimeOut)

    LaunchedEffect(true){
        delay(1000)
        currentOnTimeout()
    }

    /**
     * When LaunchedEffect enters the composition it will launch block into the composition's CoroutineContext.
     * The coroutine will be cancelled and re-launched when LaunchedEffect is recomposed with a different key1.
     *
     * Here, we are passing key1 = true, it means key1 is constant.
     * LaunchedEffect will launch coroutine when it will enters the composition. Again, In
     * recomposition, LaunchedEffect will not launch because we passed key1 = true constant value and it not changed.
     * If key1 will changed, LaunchedEffect will launch again.
     *
     *
     * rememberUpdatedState(onTimeOut)
     * To make sure, lambada contains the latest value that composable with recomposed, use rememberUpdatedState()
     * will use the updated lambada only.
     *
     * NOTE : always use rememberUpdatedState() while passing lambada to composable.
     *
     */

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START){
                //start
            } else if(event == Lifecycle.Event.ON_STOP){
                //stop
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /**
     * It same as LaunchedEffect. But different is, before launching a new job,
     * it make sure that old things should cleaned up.
     */

    val firebaseAnalytics = remember { mutableStateOf("")}
    SideEffect {
//        firebaseAnalytics.push(value)
    }

    /**
     * Side Effect -
     * Schedule effect to run when the current composition completes successfully and applies changes.
     */

    val networkUsers = produceState(initialValue = Resource.Loading){
        //val res = withContext(Dispatchers.IO) { repo.getUsers() }
        //if(res.status == SUCCESS) return Resource.Success(res.data)
        //else return Resource.error(msg)
    }

    /**
     * produceState - used for change non composable state to composable state
     * use it as replace for flow/livedata/Rx
     */


}

sealed class Resource {
    object Loading : Resource()
    object Error : Resource()
    object Success : Resource()
}

data class User(
    var name: String
)
