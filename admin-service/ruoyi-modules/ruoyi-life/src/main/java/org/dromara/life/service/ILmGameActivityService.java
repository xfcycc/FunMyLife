package org.dromara.life.service;

import org.dromara.life.domain.bo.LmGameActivityBo;
import org.dromara.life.domain.vo.LmGameActivityVo;

import java.util.List;

/**
 * 游戏活动 服务层
 */
public interface ILmGameActivityService {

    /**
     * 查询活动列表
     *
     * @param projectId 项目ID
     * @return 活动列表
     */
    List<LmGameActivityVo> queryList(Long projectId);

    /**
     * 查询活动详情
     *
     * @param activityId 活动ID
     * @return 活动信息
     */
    LmGameActivityVo queryById(Long activityId);

    /**
     * 新增活动
     *
     * @param bo 活动信息
     */
    void insert(LmGameActivityBo bo);

    /**
     * 修改活动
     *
     * @param bo 活动信息
     */
    void update(LmGameActivityBo bo);

    /**
     * 删除活动
     *
     * @param activityId 活动ID
     */
    void deleteWithValid(Long activityId);

    /**
     * 切换活动提醒状态
     *
     * @param activityId 活动ID
     * @param enabled    是否启用
     */
    void toggleReminder(Long activityId, boolean enabled);
}
