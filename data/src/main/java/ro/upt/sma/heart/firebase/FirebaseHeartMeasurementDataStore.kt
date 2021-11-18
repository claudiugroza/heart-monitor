package ro.upt.sma.heart.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import ro.upt.sma.heart.model.HeartMeasurement
import ro.upt.sma.heart.model.HeartMeasurementRepository

class FirebaseHeartMeasurementDataStore(userId: String) : HeartMeasurementRepository {

    private val reference: DatabaseReference = Firebase.database.reference.child(userId)

    override fun post(heartMeasurement: HeartMeasurement) {
        // Post the new value to firebase
        reference.child(heartMeasurement.timestamp.toString()).setValue(heartMeasurement.value)
    }

    override fun observe(listener: HeartMeasurementRepository.HeartChangedListener) {
        // Add a child event listener and pass the last value to the listener
        reference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                listener.onHeartChanged(toHeartMeasurement(snapshot))
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Not yet implemented
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Not yet implemented
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Not yet implemented
            }

            override fun onCancelled(error: DatabaseError) {
                // Not yet implemented
            }

        })
    }

    override fun retrieveAll(listener: HeartMeasurementRepository.HeartListLoadedListener) {
        // Retrieve all measurements and pass them to the listener
        reference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                listener.onHeartListLoaded(toHeartMeasurements(children))
                reference.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
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
                java.lang.Long.valueOf(dataSnapshot.key.toString()),
                dataSnapshot.getValue(Int::class.java)!!)
    }

}
