package com.example.gamecompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import com.example.gamecompanion.fragments.ChatFragment
import com.example.gamecompanion.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

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
                    // Analytics
                    Firebase.analytics.logEvent("CompsTabClicked", null)
                }
                R.id.chatTab->{
                    // Analytics
                    Firebase.analytics.logEvent("ChatTabClicked", null)
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer,
                        ChatFragment()
                    )
                    transaction.commit()
                }
                R.id.streamsTab->{
                    // Analytics
                    Firebase.analytics.logEvent("StreamsTabClicked", null)
                }
                R.id.patchTab->{
                    // Analytics
                    Firebase.analytics.logEvent("PatchTabClicked", null)
                }
                R.id.userTab->{
                    // Analytics
                    Firebase.analytics.logEvent("UserTabClicked", null)
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainer,
                        ProfileFragment()
                    )
                    transaction.commit()
                }

            }
            true
        }
        // Tab inicial
        // Analytics landing
        // Analytics
        Firebase.analytics.logEvent("LandingOpenedApp", null)
        bottomNavView.selectedItemId = R.id.userTab
    }
}