package com.example.robotjoystick.view.bluetoothdevice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.robotjoystick.R
import com.example.robotjoystick.data.bluetooth.scanner.BluetoothDeviceData
import com.example.robotjoystick.databinding.BluetoothDeviceDataItemBinding

class BluetoothDeviceDataArrayAdapter(
    context: Context,
    private val clickCallback: (BluetoothDeviceData) -> Unit
) : ArrayAdapter<BluetoothDeviceData>(context, R.layout.bluetooth_device_data_item, ArrayList()) {

    class BluetoothDeviceDataViewHolder(
        val root: View,
        private val clickCallback: (BluetoothDeviceData) -> Unit
    ) {
        lateinit var item: BluetoothDeviceData
        lateinit var tvName: TextView
        lateinit var tvAddress: TextView
        lateinit var icon: ImageView

        init {
            root.setOnClickListener { clickCallback(item) }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder = convertView?.tag as? BluetoothDeviceDataViewHolder
        if (viewHolder == null) {
            val binding = BluetoothDeviceDataItemBinding.inflate(LayoutInflater.from(context), parent, false)
            viewHolder = BluetoothDeviceDataViewHolder(binding.root, clickCallback)
            viewHolder.tvName = binding.title
            viewHolder.tvAddress = binding.subtitle
            viewHolder.icon = binding.statusIcon
            convertView?.tag = viewHolder
        }
        val item = getItem(position)!!
        viewHolder.item = item
        viewHolder.tvName.text = item.name
        viewHolder.tvAddress.text = item.address
        viewHolder.icon.visibility = if (item.bondState == BluetoothDeviceData.BondState.BONDED) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        return viewHolder.root
    }

    fun submitList(data: List<BluetoothDeviceData>) {
        clear()
        addAll(data)
        notifyDataSetChanged()
    }
}

