package com.excelreader.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    private Integer audit_id;
    private Description description;
    private String detail_result;
    private String module;
    private String operate_event;
    private String operate_ip;
    private String operate_object;
    private String operate_result;
    private Date operate_time;
    private String operator;
    private String service_name;
    private Integer sn;
}
