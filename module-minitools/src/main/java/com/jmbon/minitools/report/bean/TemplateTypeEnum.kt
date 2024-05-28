package com.jmbon.minitools.report.bean

/******************************************************************************
 * Description: 公共模板类型
 *
 * Author: jhg
 *
 * Date: 2023/4/22
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
enum class TemplateTypeEnum(val value: Int) {
    /**
     * 默认模板
     */
    TEMPLATE_TYPE_DEFAULT(0),

    /**
     * 生育力自测
     */
    TEMPLATE_TYPE_FERTILITY_ABILITY_TEST(1),

    /**
     * 试管自测
     */
    TEMPLATE_TYPE_TUBE_TEST(2)
}