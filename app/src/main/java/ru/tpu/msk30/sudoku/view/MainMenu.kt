package ru.tpu.msk30.sudoku.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main_menu.*
import ru.tpu.msk30.sudoku.R
import ru.tpu.msk30.sudoku.game.Difficulty.DifficultuLevel
import ru.tpu.msk30.sudoku.game.Difficulty.HighDifficulty
import ru.tpu.msk30.sudoku.game.Difficulty.LowDifficulty
import ru.tpu.msk30.sudoku.game.Difficulty.MediumDifficulty

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
        }
    }
}
