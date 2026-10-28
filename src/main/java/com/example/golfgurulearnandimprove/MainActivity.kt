package com.example.golfgurulearnandimprove


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList
import android.text.util.Linkify;
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var valueTextView: TextView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private lateinit var addNewButton: Button
    private lateinit var scoreEditText: EditText
    private  val stackOfLists: Stack<ArrayList<Int>> = Stack()


    private lateinit var backButton: Button
    private var currentIndex = 0
    private var stackOfClubs: Stack<String> = Stack()
    private var currentIndex1 = 0
    private var skillLevel: String? = "Beginner"

    private var count: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check if the user's name exists in SharedPreferences
        sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val savedName = sharedPreferences.getString("name", null)
        if (savedName != null) {
            // Name already exists, proceed to main activity
            startMain()
            return
        }

        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.nameEditText)
        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            if (name.isNotBlank()) {
                // Save the name to SharedPreferences
                sharedPreferences.edit().putString("name", name).apply()
                setContentView(R.layout.prehomescreen)
                val buttonX: Button = findViewById(R.id.startButton)
                buttonX.setOnClickListener{
                    startMain()
                }
            } else {
                // Handle empty name input
                // You can show a toast or error message here
            }
        }
    }

    val backTrack = Stack<String>()
    @SuppressLint("MissingInflatedId")
    public fun startMain() {
/*
        setContentView(R.layout.prehomescreen)
        val buttonX: Button = findViewById(R.id.startButton)
        buttonX.setOnClickListener{
            startMain()
            count++
        }*/
            setContentView(R.layout.homescreen)
            backTrack.push("homescreen")
            val scorecardButton: Button = findViewById(R.id.scorecardButton)
            val distanceTrackingButton: Button = findViewById(R.id.distanceTrackingButton)
            val videoButton: Button = findViewById(R.id.videosButton)
            val infoButton: Button = findViewById(R.id.infoButton)
            val skillLevelIndicator: TextView = findViewById(R.id.SkillLevelView)
            skillLevelIndicator.text = "User's Current Skill Level: " + skillLevel
            scorecardButton.setOnClickListener {
                openScorecardLayout()
            }

            distanceTrackingButton.setOnClickListener {
                openDistanceTrackingLayout()
            }

            videoButton.setOnClickListener {
                openVideoLayout()
            }
            infoButton.setOnClickListener {
                openInfoLayout()
            }
            //val intent = Intent(this, HomeScreen::class.java)
            //startActivity(intent)
        }

    @SuppressLint("MissingInflatedId")
    fun openInfoLayout(){
        setContentView(R.layout.infolayout)
        val backB: Button = findViewById(R.id.backB)
        backB.setOnClickListener{
            startMain()
        }
    }
    @SuppressLint("MissingInflatedId")
    fun openScorecardLayout() {
        setContentView(R.layout.scorecard_layout)
        backTrack.push("scorecard_layout")
        valueTextView = findViewById(R.id.valueTextView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        addButton = findViewById(R.id.addButton)
        scoreEditText = findViewById(R.id.scoreEditText)
        backButton = findViewById(R.id.backB)
        deleteButton = findViewById(R.id.deleteButton)
        addNewButton = findViewById(R.id.addNewButton)
        // Initialize stack of ArrayLists with some data

        if (stackOfLists.size() == 0){
            val list = ArrayList<Int>()
          stackOfLists.push(list)
        }




        // Display initial value
        displayValue()

        // Set click listeners

        prevButton.setOnClickListener { navigatePrevious() }
        nextButton.setOnClickListener { navigateNext() }
        addButton.setOnClickListener { addToScorecard() }
        backButton.setOnClickListener {
            back() }
        deleteButton.setOnClickListener { delete() }
        addNewButton.setOnClickListener { add() }
        if(stackOfLists.size()>2) {
            val y = stackOfLists.getSecondElementOfLast()
            if (y != null) {
                if (average(stackOfLists) < 80 || y < 80) {
                    skillLevel = "Advanced"
                } else if (y != null) {
                    if (average(stackOfLists) < 90 || y < 90) {
                        skillLevel = "Intermediate"
                    } else {
                        skillLevel = "Beginner"
                    }


                }
            }
        }
    }


    fun saveStack(stack: Stack<ArrayList<Int>>, filePath: String){
        val jsonString = Json.encodeToString(stack.getElements())
        File(filePath).writeText(jsonString)
    }
    fun displayValue() {
        if (!stackOfLists.isEmpty()) {
            val currentList = stackOfLists.get(currentIndex)
            val valueString = stackOfLists.getArrayListAsString(currentIndex)
            valueTextView.text = valueString
        } else {
            valueTextView.text = "No data"
        }
    }

    fun delete() {
        stackOfLists.replace(currentIndex, ArrayList<Int>())
        displayValue()
    }

    private fun navigatePrevious() {
        if (currentIndex > 0) {
            currentIndex--
            displayValue()
        }
    }

    private fun navigateNext() {
        if (currentIndex < stackOfLists.size() - 1) {
            currentIndex++
            displayValue()
        }
    }

    private fun add() {
        stackOfLists.push(ArrayList<Int>())
    }

    private fun addToScorecard() {
        val scoreText = scoreEditText.text.toString()
        if (scoreText.isNotBlank()) {
            val score = scoreText.toInt()
            if (currentIndex == stackOfLists.size()) {
                // If currentIndex is at the end, create a new ArrayList
                val newList = arrayListOf(score)
                stackOfLists.push(newList)
                currentIndex++
            } else {
                val x = stackOfLists.get(currentIndex)
                if (x != null) {
                    x.add(score)
                }
                if (x != null) {
                    stackOfLists.replace(currentIndex, x)
                }
                // Otherwise, add score to the current ArrayList
                //stackOfLists.addToArrayListAt(currentIndex, score)
            }
            displayValue()
            scoreEditText.text.clear()
        } else {
            // If the score is empty, move to the next ArrayList
            currentIndex++
            val newList = arrayListOf<Int>()
            stackOfLists.push(newList)
            displayValue()
        }

    }

    fun average(stack: Stack<ArrayList<Int>>): Double {
        var sum = 0.0
        var count = 0
        if (stackOfLists.size()<=1){
            return 100.0
        }
        // Iterate through the stack
        for (list in stack.getElements()) {
            // Check if the list is not empty
            if (list.isNotEmpty()) {
                if (list[0] == 18) {
                    sum += list[1]  // Add the first element to the sum
                    count++
                }
                if (list[0] == 9) {
                    sum += (list[1] * 2)
                    count++
                }
            }
        }

        // Calculate the average
        return if (count > 0) {
            sum / count
        } else {
            100.0 // return 0 if stack is empty or all lists are empty
        }
    }

    fun determineSkillLevel(averageScore: Double): String {
        return when {
            averageScore >= 100 -> "Beginner"
            averageScore >= 80 && averageScore < 100 -> "Intermediate"
            averageScore < 80.0 -> "Advanced"
            else -> "Invalid Score"
        }
    }

    private fun back() {
        startMain()
    }

    @SuppressLint("MissingInflatedId")
    fun openDistanceTrackingLayout() {
        setContentView(R.layout.distance_tracking)
        valueTextView = findViewById(R.id.valueTextView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        addButton = findViewById(R.id.addButton)
        scoreEditText = findViewById(R.id.scoreEditText)
        backButton = findViewById(R.id.backB)
        deleteButton = findViewById(R.id.deleteButton)
        addNewButton = findViewById(R.id.addNewButton)


        //stackOfClubs=loadStack("stackC")

        if (stackOfClubs.size()==0){
            val list1 = ""
            stackOfClubs.push(list1)
        }

        //stackOfClubs.loadFromFile(appContext,"stackC_data.json")
        displayValue1()
        prevButton.setOnClickListener { navigatePrevious1() }
        nextButton.setOnClickListener { navigateNext1() }
        addButton.setOnClickListener { addToDT1() }
        backButton.setOnClickListener {
            back() }
        deleteButton.setOnClickListener { delete1() }
        addNewButton.setOnClickListener { add1() }

    }

    private fun displayValue1() {
        if (!stackOfClubs.isEmpty()) {
            val currentList = stackOfClubs.get(currentIndex1)

            valueTextView.text = currentList
        } else {
            valueTextView.text = "No data"
        }
    }

    private fun delete1() {
        stackOfClubs.replace(currentIndex1, "")
        displayValue1()
    }

    private fun navigatePrevious1() {
        if (currentIndex1 > 0) {
            currentIndex1--
            displayValue1()
        }
    }

    private fun navigateNext1() {
        if (currentIndex1 < stackOfClubs.size() - 1) {
            currentIndex1++
            displayValue1()
        }
    }

    private fun add1() {
        stackOfClubs.push("")
    }

    private fun addToDT1() {
        val scoreText = scoreEditText.text.toString()
        if (scoreText.isNotBlank()) {
            val score = scoreText.toString()
            if (currentIndex1 == stackOfClubs.size()) {
                // If currentIndex is at the end, create a new ArrayList
                val newString = score
                stackOfClubs.push(newString.toString())
                currentIndex1++
            } else {
                val x = stackOfClubs.get(currentIndex1) + score.toString()
                stackOfClubs.replace(currentIndex1, x)
            }
            displayValue1()
            scoreEditText.text.clear()
        } else {
            // If the score is empty, move to the next ArrayList
            currentIndex1++
            val newString = String
            stackOfClubs.push(newString.toString())
            displayValue1()
        }
        //stackOfClubs.saveToFile(appContext,"stackC_data.json")
    }

    @SuppressLint("MissingInflatedId")
    fun openVideoLayout() {
        setContentView(R.layout.videos)
        val beginnerButton: Button = findViewById(R.id.btnBeginner)
        val intermediateButton: Button = findViewById(R.id.btnIntermediate)
        val advancedButton: Button = findViewById(R.id.btnAdvanced)
        val videoLinkTextView: TextView = findViewById(R.id.txtVideoLink)
        val backButton1: Button = findViewById(R.id.btnBack)

        backButton1.setOnClickListener {

            startMain()
        }
        val y = stackOfLists.getSecondElementOfLast()
        beginnerButton.setOnClickListener {
            if (y != null) {
                if (average(stackOfLists) > 80 || y>80) {
                    skillLevel = "Intermediate"
                }
                if (average(stackOfLists) > 90 || y>90){
                    skillLevel = "Beginner"
            }
             if (average(stackOfLists)<80 || y<80){ skillLevel = "Advanced"}
            }
            setContentView(R.layout.beginner_videos) // Set layout for beginner videos
            val beginnerVideosTextView: TextView = findViewById(R.id.txtBeginnerVideos)
            beginnerVideosTextView.text =
                "Introduction to Golf Guru: https://youtu.be/JJJ56X6GGY8?si=SKP9CMkCSuYmhlKo\n" +"------------------------------------------------------------------\n"+
                        "Grip and Stance: https://youtu.be/ju9WLOZDu3I?si=LfJlaaDzrU8CXY-K\n" +"------------------------------------------------------------------\n"+
                        "Explanation of Swing: https://youtu.be/LzCLry1oV2s?si=MEq_q2V4UcIBsuss\n" +"------------------------------------------------------------------\n"+
                        "Beginner Chipping Video: https://youtu.be/0tSf6b69v7c?si=TyGNjqrUp3k3nSCs\n" +"------------------------------------------------------------------\n"+
                        "Beginner Chipping Video Explaining Club Face Manipulation: https://youtu.be/7gGj4WgMkg4?si=p-d9t-85df5mpPw3\n" +"------------------------------------------------------------------\n"+
                        "Beginner Putting Techniques: https://youtu.be/OqK8C94hDIk?si=aH-zVh9SUdJQwIHt\n" +"------------------------------------------------------------------\n"+
                        "Pre-Shot Routine:https://youtu.be/ju9WLOZDu3I?si=LfJlaaDzrU8CXY-K\n"+"------------------------------------------------------------------"

            Linkify.addLinks(beginnerVideosTextView, Linkify.WEB_URLS) // Make links clickable

            val backButtonBeginner: Button = findViewById(R.id.btnBackBeginner)
            backButtonBeginner.setOnClickListener {
                openVideoLayout()
            }
        }
        //if (skillLevel.equals("Intermediate") || skillLevel.equals("Advanced"))

        if ( y != null) {
            if (average(stackOfLists) < 90 || y < 90) {
                intermediateButton.setOnClickListener {
                    if (average(stackOfLists) < 80 || y < 80) {
                        skillLevel = "Advanced"
                    } else {
                        skillLevel = "Intermediate"
                    }
                    setContentView(R.layout.intermediate_videos)
                    val backButtonIntermediate: Button = findViewById(R.id.btnBackIntermediate)
                    backButtonIntermediate.setOnClickListener {
                        openVideoLayout()
                    }
                    val intermediateVideosTextView: TextView =
                        findViewById(R.id.txtIntermediateVideos)
                    intermediateVideosTextView.text = "Understanding of golf swing and common issues: https://youtu.be/k9KaH1FfqM4?si=a0O81052yH7Pk7X3\n"+"------------------------------------------------------------------\n"+
                            "How to correct pushes, push slices, and push hooks: https://youtu.be/pN4XERGAm4k?si=kuvBUgSYwFr_cw-Y\n"+"------------------------------------------------------------------\n"+
                            "Explanation of the shank shot and how to prevent it: https://youtu.be/9lAUn-vEZlU?si=VYJNL2I1v95Mal5m \n"+"------------------------------------------------------------------\n"+
                            "Explanation of pulls, hooks, and how to fix it: https://youtu.be/2t0G2PPHs3g?si=PjTkGt0kBKqhVWbI\n "+"------------------------------------------------------------------\n"+
                            "How to fix chunky and thin shots: https://youtu.be/_qrRL1eU8OU?si=e9RY5JKwPHo9_7sE\n" +"------------------------------------------------------------------\n"+
                            "How to hit a ball on an upslope: https://youtu.be/3l49F6tnSbU?si=dQ9NY5e1yvI5wLx6\n"+"------------------------------------------------------------------\n"+
                            "How to hit a ball laying under your feet: https://youtu.be/6EofCBRdLrs?si=i0TiVCbCLc-nPPAJ\n"+"------------------------------------------------------------------\n"+
                            "How to hit a ball laying above your feet: https://youtu.be/8xfKI7AxxtY?si=DpjQX9xyaEsYOle3\n"+"------------------------------------------------------------------\n"+
                            "How to hit a ball off a downslope: https://youtu.be/rSIvjrN1jbk?si=K6J_2a0oY52aHBGW\n"+"------------------------------------------------------------------\n"+
                            "Intermediate putting techniques: https://youtu.be/wErtktCeKPY?si=uMXe1l_tKuoRJDbl\n"+"------------------------------------------------------------------\n"+
                            "Dealing with greenside bunkers: https://youtu.be/hH1AS1RrTgM?si=FTZYzSaTTWdGTtZt\n"+"------------------------------------------------------------------\n"+
                            "Intermediate chipping techniques: https://youtu.be/37VHYAxHM0Y?si=fwe2RWUXOKl9Q0Se\n"+"------------------------------------------------------------------\n"+
                            "How to hit the ball farther: https://youtu.be/s8BuA68Fzbc?si=1KNGSxGuVbd9is9P\n"+"------------------------------------------------------------------"

                    Linkify.addLinks(intermediateVideosTextView, Linkify.WEB_URLS)
                }
            }
        }
        //if (skillLevel.equals("Advanced")) {

        if (y != null) {
            if (average(stackOfLists)<80 || y < 80){
                skillLevel = "Advanced"
                advancedButton.setOnClickListener {
                    setContentView(R.layout.advanced_videos)
                    val backButtonAdvanced: Button = findViewById(R.id.btnBackAdvanced)
                    backButtonAdvanced.setOnClickListener {
                        openVideoLayout()
                    }
                    val advancedVideosTextView: TextView = findViewById(R.id.txtAdvancedVideos)
                    advancedVideosTextView.text = "Understanding your shot type: https://youtu.be/EfRlnLH2Irw?si=5zDKiISMDyqbuh-n\n"+"------------------------------------------------------------------\n"+
                            "How to hit a draw on command: https://youtu.be/LiW9KsWq1Tw?si=mxGKQcgYSQmeTwZ1\n"+"------------------------------------------------------------------\n"+
                            "How to hit a fade/cut on command: https://youtu.be/3zEgPsoe2W0?si=alM7THf1ZSDl0v3m\n"+"------------------------------------------------------------------\n"+
                            "How to hit flop shots and when to use them: https://youtu.be/Z0XjAk1ac2c?si=o4K6PVCZwguaUurk\n"+"------------------------------------------------------------------\n"+
                            "How to hit stingers and deal with windy courses: https://youtu.be/e4JKaiKZYS8?si=jzUPocE8fnMzT0d8\n"+"------------------------------------------------------------------\n"+
                            "How to hit high pop shots out of greenside bunkers: https://youtu.be/od_LQPVbpyI?si=h-eeBzgNL6MITqg3\n"+"------------------------------------------------------------------"
                    Linkify.addLinks(advancedVideosTextView, Linkify.WEB_URLS)
                }

                // Make the links clickable
                Linkify.addLinks(videoLinkTextView, Linkify.WEB_URLS)
            }
        }
    }
}





