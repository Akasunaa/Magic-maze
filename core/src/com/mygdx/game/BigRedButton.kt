package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Net
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.net.SocketHints
import java.io.IOException
import java.lang.System.load



class BigRedButton(idleTexture: Texture, pushedTexture: Texture, x:Float, y:Float, width:Float, height:Float) {
    var x:Float
    var y:Float
    var width:Float
    var height:Float
    var counter:Int = 0

    val idle :  Sprite = Sprite(idleTexture)
    val pushed: Sprite = Sprite(pushedTexture)
    var active:Sprite = idle



    init {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        updateSprite()
    }

    fun updateSprite() {
        idle.setPosition(x,y)
        pushed.setPosition(x,y)
        idle.setSize(width, height)
        pushed.setSize(width, height)
    }

    fun setPosition(x:Float, y:Float) {
        this.x = x
        this.y = y
    }

    fun stringPosition (): String {
        return "x = $x; y = $y"
    }

    fun setSize(width:Float, height:Float) {
        this.width = width
        this.height = height
    }
    fun update(batch: SpriteBatch) {
        active.draw(batch)
    }
    fun isClicked(iX:Float, iY:Float, id:String, ip:String) {
        if (x <= iX && iX <= x + width) {
                if (y <= iY && iY <= y + height) {
                    val textToSend = "Button Pressed by $id! \n"
                    val socketHints = SocketHints()
                    socketHints.connectTimeout = 4000
                    val socket = Gdx.net.newClientSocket(Net.Protocol.TCP,ip,9021,socketHints)
                    try {
                        socket.getOutputStream().write(textToSend.toByteArray())
                    } catch(e: IOException) {
                        e.printStackTrace()
                    }
                    active = pushed
                    counter++
                }
            }
        }

    fun isClickable(): Boolean {
        if (counter > 30) {
            counter = 0
            active = idle
        }
        if (counter == 0) return true
        else {
            counter ++
            return false
        }
    }

    fun dispose() {
        idle.texture.dispose()
        pushed.texture.dispose()
    }
}