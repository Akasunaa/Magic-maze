package tsp.genint.multiplayer

import java.util.ArrayList

class ClientList : MutableList<Client> by mutableListOf() {
    fun getClientFromPseudo(pseudo: String) = first { it.id == pseudo }
}