package ro.upt.sma.heart.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList
import ro.upt.sma.heart.model.HeartMeasurement
import ro.upt.sma.heart.model.HeartMeasurementRepository

class FirebaseHeartMeasurementDataStore(userId: String) : HeartMeasurementRepository {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference.child(userId)

    override fun post(heartMeasurement: HeartMeasurement) {

        reference.child(heartMeasurement.timestamp.toString()).setValue(heartMeasurement.value)

    }

    override fun observe(listener: HeartMeasurementRepository.HeartChangedListener) {

        reference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, string: String?) {
                listener.onHeartChanged(toHeartMeasurement(snapshot))
            }

            override fun onChildChanged(snapshot: DataSnapshot, string: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, string: String?) { }

            override fun onCancelled(snapshot: DatabaseError) { }

        })

    }

    override fun retrieveAll(listener: HeartMeasurementRepository.HeartListLoadedListener) {
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children

                listener.onHeartListLoaded(toHeartMeasurements(children))

                reference.removeEventListener(this)
            }

            override fun onCancelled(snapshot: DatabaseError) {
                reference.removeEventListener(this)
            }
        })
    }


    private fun toHeartMeasurements(children: Iterable<DataSnapshot>): List<HeartMeasurement> {
        val heartMeasurementList = ArrayList<HeartMeasurement>()

        for (child in children) {
            heartMeasurementList.add(toHeartMeasurement(child))
        }

        return heartMeasurementList
    }

    private fun toHeartMeasurement(dataSnapshot: DataSnapshot): HeartMeasurement {
        return HeartMeasurement(
                java.lang.Long.valueOf(dataSnapshot.key),
                dataSnapshot.getValue(Int::class.java)!!)
    }

}
