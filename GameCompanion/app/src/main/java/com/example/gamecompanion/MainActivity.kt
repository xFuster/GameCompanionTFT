package com.example.gamecompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Variables find views
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavBar)

        // Get Fragment Container
        val fragmentContainer:FrameLayout = findViewById(R.id.fragmentContainer)

        // Tab selected
        bottomNavView.setOnNavigationItemSelectedListener {menuItem: MenuItem ->
            when(menuItem.itemId){
                R.id.compsTab->{

                }
                R.id.chatTab->{
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer, ChatFragment())
                    transaction.commit()
                }
                R.id.streamsTab->{

                }
                R.id.patchTab->{

                }
                R.id.userTab->{
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer,ProfileFragment())
                    transaction.commit()
                }

            }
            true
        }
        // Tab inicial
        bottomNavView.selectedItemId = R.id.patchTab
    }
}