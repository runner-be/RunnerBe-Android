package com.applemango.runnerbe.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

class NavigationViewModel : ViewModel() {
    val popBackStack = MutableLiveData<Boolean>()
    val navSpecificBackStack = MutableLiveData<Int>()
    val navDirectionAction = MutableLiveData<NavDirections?>()
}