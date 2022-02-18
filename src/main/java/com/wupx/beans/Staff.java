package com.wupx.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Miles
 * @version 1.0.0
 * @Description Staff
 * <p>
 * @ClassName Sinject.java
 * @createTime 2022-02-10 13:18:00
 */
@Data
@Builder
@AllArgsConstructor
public class Staff  {
    private String employeeId;
    private String name;
    private String email;
    private String subject;
}
