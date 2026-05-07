package com.funmylife.fml.domain.repository;

import com.funmylife.fml.domain.model.*;
import com.funmylife.fml.domain.block.BlockInstance;

import java.util.List;

/**
 * Life Manager 领域数据仓储接口。
 *
 * <p>application 层只能依赖这个接口读取或保存领域模型，不能直接感知
 * MyBatis-Plus、Mapper、表实体等基础设施细节。这样 Controller -> Application ->
 * Domain Repository -> Infrastructure 的依赖方向是单向的，避免持久化 Entity 被误传到接口层。</p>
 */
public interface LifeDataRepository {

    /** 按项目 ID 查询项目聚合根，未找到返回 null。 */
    LmProject findProjectById(Long projectId);

    /** 查询项目下所有功能块实例，用于管理页和详情页动态渲染。 */
    List<BlockInstance> findBlockInstances(Long projectId);

    /** 按项目 ID + 功能块 key 查询单个功能块实例，主要用于概览摘要规则加载和 upsert。 */
    BlockInstance findBlockInstance(Long projectId, String blockKey);

    /** 新增功能块实例，入参是领域模型，具体表对象由 infrastructure 负责转换。 */
    void insertBlockInstance(BlockInstance blockInstance);

    /** 更新功能块实例，保持 application 层不直接依赖 Mapper。 */
    void updateBlockInstance(BlockInstance blockInstance);

    /** 查询项目下所有游戏版本。 */
    List<LmGameVersion> findGameVersions(Long projectId);

    /** 查询项目当前 active 游戏版本，未找到返回 null。 */
    LmGameVersion findCurrentGameVersion(Long projectId);

    /** 查询项目下所有版本活动。 */
    List<LmGameActivity> findGameActivities(Long projectId);

    /** 查询项目下所有目标任务。 */
    List<LmGameTarget> findGameTargets(Long projectId);

    /** 按目标 ID 查询单个目标，调用方需要再校验 projectId 归属。 */
    LmGameTarget findGameTargetById(Long targetId);

    /** 保存目标状态或进度变更。 */
    void updateGameTarget(LmGameTarget target);

    /** 查询项目时间轴事件，默认由实现层按发生时间倒序返回。 */
    List<LmTimelineEvent> findTimelineEvents(Long projectId);

    /** 查询素材收集数据。 */
    List<LmMaterial> findMaterials(Long projectId);

    /** 查询图册列表。 */
    List<LmAlbum> findAlbums(Long projectId);

    /** 统计项目图册数量，用于概览摘要。 */
    int countAlbums(Long projectId);

    /** 查询照片列表，默认由实现层按创建时间倒序返回。 */
    List<LmPhoto> findPhotos(Long projectId);

    /** 查询账号资产列表。 */
    List<LmAsset> findAssets(Long projectId);

    /** 按资产 ID 查询单个资产，调用方需要再校验 projectId 归属。 */
    LmAsset findAssetById(Long assetId);

    /** 查询笔记列表。 */
    List<LmNote> findNotes(Long projectId);

    /** 按笔记 ID 查询单篇笔记，调用方需要再校验 projectId 归属。 */
    LmNote findNoteById(Long noteId);
}
