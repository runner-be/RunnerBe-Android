package com.applemango.runnerbe.presentation.screen.dialog.weather

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.ItemStampBinding
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampAdapter.*
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherAdapter.*
import com.bumptech.glide.Glide

class WeatherAdapter : ListAdapter<WeatherItem, WeatherViewHolder>(stampDiffUtil) {
    private lateinit var onWeatherClickListener: OnWeatherClickListener
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherViewHolder(ItemStampBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, onWeatherClickListener)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedWeather(selectedWeather: WeatherItem?) {
        // 기존 선택된 날씨가 있으면 해당 position을 찾음
        val index = currentList.indexOfFirst { it.name == selectedWeather?.name }
        if (index != -1) {
            selectedPosition = index
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateSelectedPosition(position: Int) {
        Log.e("UpdateSelectedPosition : ", "selectedPosition : $selectedPosition | newPosition: $position")
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun setOnWeatherClickListener(listener: OnWeatherClickListener) {
        this.onWeatherClickListener = listener
    }

    inner class WeatherViewHolder (
        private val binding: ItemStampBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherItem, onStampClickListener: OnWeatherClickListener) {
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivStamp)

            if (bindingAdapterPosition == selectedPosition) {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_primary_stroke)
            } else {
                binding.flStamp.setBackgroundResource(R.drawable.bg_g5_circle_shape_no_stroke)
            }
            binding.flStamp.setOnClickListener {
                updateSelectedPosition(bindingAdapterPosition)
                onStampClickListener.onWeatherSelected(item)
            }
        }
    }

    companion object {
        private val stampDiffUtil = object : DiffUtil.ItemCallback<WeatherItem>() {
            override fun areItemsTheSame(
                oldItem: WeatherItem,
                newItem: WeatherItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: WeatherItem,
                newItem: WeatherItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}