package com.example.luiseduardovelaruiz.hopping

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        imageButton4.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@MenuActivity,"clicked a",Toast.LENGTH_LONG).show()
        })
        imageButton5.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@MenuActivity,"clicked b",Toast.LENGTH_LONG).show()
        })
        imageButton6.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@MenuActivity,"clicked c",Toast.LENGTH_LONG).show()
        })

    }//end onCreate
}//end class MenuActivity