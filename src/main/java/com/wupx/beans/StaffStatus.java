package com.wupx.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miles
 * @version 1.0.0
 * @Description StaffStatus
 * <p>
 * @ClassName StaffStatus.java
 * @createTime 2022-02-15 15:00:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffStatus {
    private String employeeId;
    private String name;
    private String email;
    private String status;
}
