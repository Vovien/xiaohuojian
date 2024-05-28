package com.jmbon.minitools.pregnanttips.util

import com.blankj.utilcode.util.Utils
import java.io.IOException


object PregnantTipHtmlTools {
    var initAdviceJs = getJs("pregnant_tips_advice.js")
    var initAnswerJs = getJs("pregnant_tips_answer.js")
    var pregnantCss = getJs("pregnant_tip.css")
    var pregnantCss2 = getJs("pregnant_tip2.css")

    fun createPregnantTipsAdviceHtml(content: String): String {

        val html = content.replace("\n", "")
            .replace("\r", "")
            .replace("width:720px", "width: 100%")
            .replace("width:360px", "width: 50%")
            .replace("border: 2px solid transparent", "")
            .replace("contenteditable=\"false\"", "")
            .replace("<video", "<video  controlslist=\"nodownload noremote footbar nofullscreen\"")
        val builder = StringBuilder()
        builder.append(
            """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0, user-scalable=0">
    <title>Document</title>
     <script type="text/javascript">     $initAdviceJs    </script>
         <style type="text/css">    $pregnantCss2     </style>
</head>
<body><div>
        """.trimIndent()
        ).append(
            html
        )

        return builder.toString()
    }

    fun createPregnantTipsAnswerHtml(content: String): String {

        val html = content
            .replace("\n", "")
            .replace("\r", "")
            .replace("width:720px", "width: 100%")
            .replace("width:360px", "width: 50%")
            .replace("border: 2px solid transparent", "")
            .replace("contenteditable=\"false\"", "")
            .replace("<video", "<video  controlslist=\"nodownload noremote footbar nofullscreen\"")
        val builder = StringBuilder()
        builder.append(
            """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0, user-scalable=0">
    <title>Document</title>
     <script type="text/javascript">     $initAnswerJs    </script>
         <style type="text/css">    $pregnantCss     </style>
</head>
<body><div>
        """.trimIndent()
        ).append(
            html
        )

        return builder.toString()
    }

    private fun getJs(fileName: String): String {
        return try {
            val inputStream = Utils.getApp().assets.open(fileName)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes, 0, bytes.size)
            String(bytes)
        } catch (e: IOException) {
            ""
        }
    }


}