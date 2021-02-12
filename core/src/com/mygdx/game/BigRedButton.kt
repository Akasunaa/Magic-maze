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



class BigRedButton(idleTexture: Texture, pushedTexture: Texture, x:Float, y:Float, width:Float, height:Float, cooldown: Int) {
    var x:Float
    var y:Float
    var width:Float
    var height:Float

    val idle :  Sprite = Sprite(idleTexture)
    val pushed: Sprite = Sprite(pushedTexture)
    var active:Sprite = idle

    var startTime:Long = 0 // Utile pour le cooldown
    val cooldown:Int

    init {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.cooldown = cooldown
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
    fun setSize(width:Float, height:Float) {
        this.width = width
        this.height = height
    }

    fun stringPosition (): String {
        return "x = $x; y = $y"
    }

    fun update(batch: SpriteBatch) {
        active.draw(batch)
    }

    fun sendMessage(message:String, ip:String, port:Int) {
        val socketHints = SocketHints()
        socketHints.connectTimeout = 4000
        val socket = Gdx.net.newClientSocket(Net.Protocol.TCP,ip,port,socketHints)
        try {
            socket.getOutputStream().write(message.toByteArray())
        } catch(e: IOException) {
            e.printStackTrace()
        }
    }

    fun isClickedLocally(iX:Float, iY:Float, id:String, ip:String,port:Int) {
        if (x <= iX && iX <= x + width) {
                if (y <= iY && iY <= y + height) {
                    // Envoie du message
                    val textToSend = "Button Pressed by $id! \n"
                    sendMessage(textToSend,ip,port)
                    // Fin de l'envoi du message
                    active = pushed
                    startTime = System.currentTimeMillis()
                }
            }
        }

    fun isClickedRemotely() {
        active = pushed
        startTime = System.currentTimeMillis()
    }

    fun isClickable(): Boolean {
        if (System.currentTimeMillis() - startTime > cooldown) {
            active = idle
            return true
        } else return false
    }

    fun dispose() {
        idle.texture.dispose()
        pushed.texture.dispose()
    }
}