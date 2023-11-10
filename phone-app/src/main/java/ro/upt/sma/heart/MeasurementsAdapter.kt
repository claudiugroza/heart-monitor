package ro.upt.sma.heart


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ro.upt.sma.heart.MeasurementsAdapter.HeartMeasurementHolder
import ro.upt.sma.heart.model.HeartMeasurement
import java.text.SimpleDateFormat
import java.util.*

class MeasurementsAdapter internal constructor(private val heartMeasurements: List<HeartMeasurement>)
    : RecyclerView.Adapter<HeartMeasurementHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartMeasurementHolder {
        return HeartMeasurementHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_measurement, parent, false))
    }

    override fun onBindViewHolder(holder: HeartMeasurementHolder, position: Int) {
        val heartMeasurement = heartMeasurements[position]
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = df.format(Date(heartMeasurement.timestamp))
        holder.bind(heartMeasurement.value.toString() + "bpm", formattedDate)
    }

    override fun getItemCount(): Int {
        return heartMeasurements.size
    }

    inner class HeartMeasurementHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value: String, date: String) {
            itemView.findViewById<TextView>(R.id.tv_measurement_item_value).text = value
            itemView.findViewById<TextView>(R.id.tv_measurement_item_date).text = date
        }

    }

}
