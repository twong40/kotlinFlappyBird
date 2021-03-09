package org.game.flappy

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
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
    val positionY: Float = (60..1220).random().toFloat(),
    var gap: Float = 150f,
    var positionX: Float = 1280f,
)

class FlappyBird: KtxApplicationAdapter {
    private lateinit var renderer: ShapeRenderer
    private var player = Bird(400f)
    private var wallsOnScreen = emptyList<Walls>()

    override fun create() {
        renderer = ShapeRenderer()
    }

    override fun render() {
        handleInput()
        logic()
        draw()
    }

    private fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player = Bird(player.position + 20f)
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player = Bird(player.position - 20f)
        }
    }
    private fun logic() {
        if (Math.random() > 0.95) {
            wallsOnScreen = wallsOnScreen + Walls()
        }
        wallsOnScreen.forEach {
            it.positionX -= 1f
        }
        player = Bird(player.position - 1f)
    }
    private fun draw() {
        clearScreen(0f, 0f, 0f, 0f)

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            renderer.color = Color.GREEN
            wallsOnScreen.forEach {
                renderer.rect(it.positionX, it.positionY, 60f, 60f)
            }
        }

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            renderer.color = Color.RED
            renderer.rect(400f, player.position, 40f, 40f)
        }
    }
}