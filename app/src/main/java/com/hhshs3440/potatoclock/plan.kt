package com.hhshs3440.potatoclock

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_plan.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.*

class enter_data(var date: String = "", var work: String = "",var finished: String = "") {

}

class plan : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        plantodone.setOnClickListener {
            intent= Intent(this,done::class.java)
            startActivity(intent)
        }

        val myAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1)//adapter
        mainListView.adapter = myAdapter //把adapter加入List
        db.collection("WorkDay").whereEqualTo("finished","否").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    myAdapter.add(
                        "編號:" + document.id + "," + "\n" + document.getString("date") + "\n" + document.getString(
                            "work"
                        )
                    )
                }
            }
        }
        datePick.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val date = c.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, year, month, day ->
                run {
                    val format = "${setDateFormat(year, month, day)}"
                    datePicked.text = format
                }
            }, year, month, date).show()
        }
        import1.setOnClickListener {
            /*存入陣列*/
            val context =
                datePicked.text.toString() + "\n" + editText.text.toString() //datePicked 日期、editText 輸入事項

            if (datePicked.text == "") {
                Toast.makeText(this, "請先選擇日期", Toast.LENGTH_SHORT).show()
            } else {
                if (context == "") {
                    Toast.makeText(this, "請輸入事項", Toast.LENGTH_SHORT).show()
                } else {
                    val enterData = enter_data()
                    enterData.date = datePicked.text.toString()
                    enterData.work = editText.text.toString()
                    enterData.finished = "否"
                    val list1: MutableList<Int> = mutableListOf()
                    db.collection("WorkDay").get().addOnSuccessListener { task ->
                        if (task.isEmpty) {
                            db.collection("WorkDay").document("1").set(enterData)
                                .addOnCompleteListener { task ->
                                    myAdapter.add("編號:1," + "\n" + enterData.date + "\n" + enterData.work)
                                }

                        } else {
                            db.collection("WorkDay").get().addOnCompleteListener { task ->
                                for (doc in task.result!!) {
                                    list1.add(doc.id.toInt())
                                }
                                db.collection("WorkDay").document((list1.max()?.plus(1)).toString())
                                    .set(enterData).addOnCompleteListener { task ->
                                        myAdapter.add(
                                            "編號:" + (list1.max()
                                                ?.plus(1)).toString() + "," + "\n" + enterData.date + "\n" + enterData.work
                                        )
                                    }
                            }
                        }
                    }
                    //把陣列加入adapter
                    editText.setText("")
                }
            }
        }
        mainListView.setOnItemClickListener { parent, view, position, id ->
            val msg = myAdapter.getItem(position)
            intent = Intent(this, timer::class.java)
            intent.putExtra("日期","123")
            intent.putExtra("事項","223")
            if (msg != null) {
                intent.putExtra("編號",msg.substring(3, msg.indexOf(",")))
            }
            startActivity(intent)
        }

        mainListView.setOnItemLongClickListener { parent, view, position, id ->
            val msg = myAdapter.getItem(position)

            alert("確定要刪除$msg 嗎?") {
                yesButton {
                    myAdapter.remove(msg)
                    if (msg != null) {
                        db.collection("WorkDay").document(
                            msg.substring(3, msg.indexOf(","))
                        ).delete()
                    }
                }
                noButton { }
            }.show()
            return@setOnItemLongClickListener true
        }
    }

    private fun setDateFormat(year: Int, month: Int, day: Int): String {
        return "$year-${month + 1}-$day"
    }
}


