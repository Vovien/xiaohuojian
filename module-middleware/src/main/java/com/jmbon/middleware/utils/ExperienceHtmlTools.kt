package com.jmbon.middleware.utils

import com.blankj.utilcode.util.Utils
import java.io.IOException


object ExperienceHtmlTools {
    var initJs = getJs("init.js")

    //    var advJs = getJs("set_ad.js")
    var experienceCss = getJs("experience.css")

    fun createExperienceHtml(content: String): String {
        val width720Regex = "width: 720px"
        val width360Regex = "width: 360px"
        val html = content.replace("\n", "")
            .replace("\r", "")
            .replace(width720Regex, "width: 100%")
            .replace(width360Regex, "width: 50%")
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
            <script type="text/javascript">     $initJs    </script>
            <style type="text/css">    $experienceCss      </style>
            </head>
            <body>
            <div class='content'>
            """.replaceIndent()
        )
            .append(html)
            .append(
                """
                </div>
                </body>
                </html>
            """.replaceIndent()
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