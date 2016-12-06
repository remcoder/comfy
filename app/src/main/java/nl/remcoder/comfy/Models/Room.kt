package nl.remcoder.comfy.Models

import java.io.Serializable

/**
 * Created by Remco Veldkamp on 11/10/16.
 */

class Room(val name: String, val temperature: Double, val humidity: Double, val ipAddress: String) : Serializable
