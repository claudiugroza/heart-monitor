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
        // TODO 4: Post the new value to Firebase.
    }

    override fun observe(listener: HeartMeasurementRepository.HeartChangedListener) {
        // TODO 5: Add a child event listener and invoke the [listener] with the latest added value.
    }

    override fun retrieveAll(listener: HeartMeasurementRepository.HeartListLoadedListener) {
        // TODO 6: Retrieve all measurements and invoke the [listener].
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
