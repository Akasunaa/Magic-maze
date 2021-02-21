package com.mygdx.game

import com.badlogic.gdx.net.Socket

class Client(val ip: String, val socket: Socket) {
    constructor (socket: Socket) : this(ip = socket.remoteAddress, socket = socket) {
    }
}