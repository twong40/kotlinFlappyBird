package org.game.flappy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use

fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = 1280
        height = 720
    }

    LwjglApplication(FlappyBird(), config)
}

data class Bird(val position: Float)
data class Walls(
    val positionY: Float = 0f,
    var gap: Float = (200..530).random().toFloat(),
    var positionX: Float = 1280f,
)

class FlappyBird: KtxApplicationAdapter {
    private lateinit var renderer: ShapeRenderer
    private lateinit var font : BitmapFont
    private lateinit var batch: SpriteBatch
    private var player = Bird(400f)
    private var wallsOnScreen = ArrayList<Walls>()
    private var wallsToDelete = ArrayList<Walls>()
    private var counter: Float = 0f
    private var isGameOver: Boolean = false
    private var points: Int = 0

    override fun create() {
        renderer = ShapeRenderer()
        font = BitmapFont()
        batch = SpriteBatch()
        font.color = Color.WHITE;
    }

    override fun render() {
        handleInput()
        logic()
        draw()
    }

    private fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player = Bird(player.position + 30f)
        }
    }
    private fun logic() {
        if (!isGameOver) {
            wallsToDelete.clear()
            if (counter > 10000000f) {
                counter = 0f
            } else {
                counter += 1f
            }
            if (counter % 175f == 0f) {
                wallsOnScreen.add(Walls())
            }
            wallsOnScreen.forEach {
                it.positionX -= 1f
                if(it.positionX <= -65f) wallsToDelete.add(it)
            }
            wallsToDelete.forEach {
                wallsOnScreen.remove(it)
            }
            if (player.position > 0f) {
                player = Bird(player.position - 1.5f)
            }
        }

    }
    private fun draw() {
        clearScreen(0f, 0f, 0f, 0f)
        if (isGameOver) {
            batch.begin()
            font.draw(batch, "GAME OVER!\nFINAL SCORE: $points", 30f, 700f)
            batch.end()
        }
        else {
            batch.begin()
            font.draw(batch, "SCORE: $points", 30f, 700f)
            batch.end()
            renderer.use(ShapeRenderer.ShapeType.Filled) {
                renderer.color = Color.GREEN
                wallsOnScreen.forEach {
                    renderer.rect(it.positionX, it.positionY, 60f, it.gap - 120f)
                    renderer.rect(it.positionX, it.positionY + it.gap, 60f, 720f)
                }
            }

            renderer.use(ShapeRenderer.ShapeType.Filled) {
                renderer.color = Color.RED
                renderer.rect(400f, player.position, 40f, 40f)
            }
        }
    }
}