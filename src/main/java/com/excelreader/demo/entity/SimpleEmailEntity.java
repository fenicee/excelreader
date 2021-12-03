package com.excelreader.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEmailEntity {

    /**

     * 主题
     */
    private String subject;

    /**

     * 主题内容
     */
    private String content;

    /**

     * 接收人邮箱列表
     */
    private String[] tos;

}
