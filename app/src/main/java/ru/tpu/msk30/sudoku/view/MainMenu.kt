package ru.tpu.msk30.sudoku.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.tpu.msk30.sudoku.R


class MainMenu : AppCompatActivity() {

    private lateinit var  difficultyButtons : List<Button>
    private var isButtonVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        difficultyButtons = listOf(lowButton, mediumButton, highButton, testButton)
        difficultyButtons.forEachIndexed { index, button ->

            button.setOnClickListener{button ->

                var intent: Intent = Intent(this, PlaySudokuActivity::class.java)

                intent.putExtra("level", index)
                startActivity(intent)


            }

        }
        startButton.setOnClickListener{
            val change :Int = if(isButtonVisible) View.INVISIBLE else View.VISIBLE
            difficultyButtons.forEach{button ->
                button.visibility = change
            }
            isButtonVisible = !isButtonVisible
            val editButtonParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            editButtonParams.addRule(RelativeLayout.BELOW, R.id.testButton)
            mainMenuLayout.removeView(leaderBoardButton)
            mainMenuLayout.addView(leaderBoardButton, editButtonParams)
        }

        leaderBoardButton.setOnClickListener {

            val intent = Intent(this, LeaderBoard::class.java)
            startActivity(intent)

        }
    }
}
