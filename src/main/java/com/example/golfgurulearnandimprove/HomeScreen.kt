package com.example.golfgurulearnandimprove



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeScreen : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homescreen)

        val scorecardButton: Button = findViewById(R.id.scorecardButton)

        scorecardButton.setOnClickListener {
            openScorecardLayout()
        }
    }

    public fun openScorecardLayout() {
        val intent = Intent(this, ScorecardActivity::class.java)
        startActivity(intent)
    }
}
