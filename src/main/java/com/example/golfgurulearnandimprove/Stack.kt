package com.example.golfgurulearnandimprove
import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

@Serializable
data class StackItem(val data: String)

public class Stack<T> {
    private val elements: MutableList<T> = mutableListOf()

    fun push(item: T) {
        elements.add(item)
    }

    fun pop(): T? {
        if (elements.isEmpty()) return null
        return elements.removeAt(elements.size - 1)
    }

    fun peek(): T? {
        return elements.lastOrNull()
    }

    fun isEmpty(): Boolean {
        return elements.isEmpty()
    }

    fun size(): Int {
        return elements.size
    }

    fun getElements(): List<T> {
        return elements
    }
    fun get(index: Int): T? {
        if (index < 0 || index >= elements.size) {
            return null
        }
        return elements[index]
    }

    fun replace(index: Int, newValue: T): Boolean {
        if (index < 0 || index >= elements.size) {
            return false
        }
        elements[index] = newValue
        return true
    }
   /* fun addToArrayListAt(index: Int, newValue: Int): Boolean {
        if (index < 0 || index >= elements.size) {
            return false
        }
        val stackItem = elements[index] as? StackItem
        stackItem?.data?.add(newValue) ?: return false
        return true
    }*/

    fun getSecondElementOfLast(): Int? {
        if (this.isEmpty()) {
            return 72
        }

        val lastList = this.peek() as? ArrayList<Int>
        return lastList?.get(1)

    }
    fun getArrayListAsString(index: Int): String? {
        val list = elements.getOrNull(index) as? ArrayList<Int>
        return list?.joinToString(separator = ", ")
    }
    fun saveStack(filePath: String) {
        val file = File(filePath)
        file.bufferedWriter().use { out ->
            elements.forEach { out.write("$it\n") }
        }
    }

    /*fun saveStack(key: String) {
        val sharedPreferences = context.getSharedPreferences("StackPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val serializedStack = elements.joinToString(",")
        editor.putString(key, serializedStack)
        editor.apply()
    }

    fun loadStack(key: String) {
        val sharedPreferences = context.getSharedPreferences("StackPrefs", Context.MODE_PRIVATE)
        val serializedStack = sharedPreferences.getString(key, null)
        if (!serializedStack.isNullOrEmpty()) {
            val deserializedStack = serializedStack.split(",").map { it.trim() }
            elements.clear()
            elements.addAll(deserializedStack as List<T>)
        }
    }*/
}

fun saveStack(stack: Stack<StackItem>, filePath: String) {
    val jsonString = Json.encodeToString(stack.getElements())
    File(filePath).writeText(jsonString)
}

fun loadStack(filePath: String): Stack<StackItem> {
    val jsonString = File(filePath).readText()
    val elements = Json.decodeFromString<List<StackItem>>(jsonString)
    val stack = Stack<StackItem>()
    elements.forEach { stack.push(it) }
    return stack
}



