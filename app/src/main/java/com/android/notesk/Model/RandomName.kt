package com.android.notesk.Model

class RandomName {
    fun getRandomName(): String? {
        var z: Int
        val sb = StringBuilder()
        var i: Int
        i = 0
        while (i < 8) {
            z = (Math.random() * 7 % 3).toInt()
            if (z == 1) { // 放數字
                sb.append((Math.random() * 10 + 48).toInt())
            } else if (z == 2) { // 放大寫英文
                sb.append((Math.random() * 26 + 65).toChar())
            } else { // 放小寫英文
                sb.append((Math.random() * 26 + 97).toChar())
            }
            i++
        }
        return sb.toString()
    }
}