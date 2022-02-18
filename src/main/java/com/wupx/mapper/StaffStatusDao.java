package com.wupx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wupx.beans.Staff;
import com.wupx.beans.StaffStatus;

import java.util.List;

/**
 * @author Miles
 * @version 1.0.0
 * @Description StaffDao
 * <p>
 * @ClassName StaffDao.java
 * @createTime 2022-02-15 18:15:00
 */
public interface StaffStatusDao extends BaseMapper<StaffStatus> {
    boolean deleteAll();
}
