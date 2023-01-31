package com.hhshs3440.potatoclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_done.*

class done : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)
        val myAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1)
        donelistview.adapter = myAdapter

        db.collection("WorkDay").whereEqualTo("finished", "是").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    var msg =
                        "編號:" + document.id + "," + "\n" + document.getString("date") + "\n" + document.getString(
                            "work"
                        )
                    myAdapter.add(msg)
                }
            }
        }


    }
}
