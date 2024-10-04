package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.Manifest
import android.Manifest.*
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentRunningAddressSearchBinding
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RunningAddressSearchFragment :
    BaseFragment<FragmentRunningAddressSearchBinding>(R.layout.fragment_running_address_search),
    View.OnClickListener {

    @Inject
    lateinit var addressAdapter: AddressAdapter
    private val viewModel: RunningAddressSearchViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[permission.ACCESS_FINE_LOCATION] == true -> {
                getUserLocation()
            }

            permissions[permission.ACCESS_COARSE_LOCATION] == true -> {
                getUserLocation()
            }

            else -> {
                Toast.makeText(context, "위치 정보 권한을 허용해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.constLocationDetect.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        initSearchEditText()
        setupAddressSearchResult()
        initAddressAdapter()
        observeLoadState()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    getAddressFromLatLng(requireContext(), latitude, longitude) { addressResult ->
                        addressResult?.let {
                            Log.e("getAddressFromLatLng", it )
                            navigateToSearchDetailFragment(
                                AddressResult(
                                    "장소 정보 없음",
                                    addressResult,
                                    latitude.toString(),
                                    longitude.toString(),
                                    1
                                )
                            )
                        }
                    }
                } else {
                    Toast.makeText(context, "위치 정보를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getAddressFromLatLng(
        context: Context,
        latitude: Double,
        longitude: Double,
        callback: (String?) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latitude,
                longitude,
                1,
                object : GeocodeListener {
                    override fun onGeocode(addressList: MutableList<Address>) {
                        if (addressList.isNotEmpty()) {
                            val address = addressList[0].getAddressLine(0)
                            callback(address)
                        } else {
                            callback(null)
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        callback(null)
                    }
                }
            )
        } else {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addressList?.get(0)?.getAddressLine(0)
            address?.let {
                callback(it)
            }
        }
    }

    private fun initAddressAdapter() {
        binding.rcvAddress.apply {
            adapter = addressAdapter.apply {
                setOnAddressClickListener { address ->
                    navigateToSearchDetailFragment(address)
                }
            }
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
        }
    }

    private fun initSearchEditText() {
        with(binding.tieSearch) {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textView.clearFocus()
                    hideKeyboard(textView)
                    fetchSearchedResults(textView.text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun fetchSearchedResults(keyword: String) {
        viewModel.getAddressSearchResultList(keyword)
    }

    private fun setupAddressSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addressResultFlow.collectLatest {
                    addressAdapter.submitData(it)
                }
            }
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            addressAdapter.loadStateFlow.collectLatest { loadStates ->
                val isResultEmpty = loadStates.refresh is LoadState.NotLoading
                        && addressAdapter.itemCount == 0
                with(binding) {
                    if (isResultEmpty) {
                        llSearchDesc.visibility = View.VISIBLE
                        rcvAddress.visibility = View.GONE
                    } else {
                        llSearchDesc.visibility = View.GONE
                        rcvAddress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun navigateToSearchDetailFragment(address: AddressResult) {
        navigate(
            RunningAddressSearchFragmentDirections.actionRunningAddressSearchFragmentToRunningAddressSearchDetailFragment(address)
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.constLocationDetect -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        permission.ACCESS_FINE_LOCATION,
                        permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
}