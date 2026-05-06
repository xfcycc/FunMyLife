package com.funmylife.fml.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.funmylife.fml.domain.model.*;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.infrastructure.persistence.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Supplier;

/**
 * Life Manager 仓储的 MyBatis-Plus 实现。
 *
 * <p>这里是 DDD 分层里唯一直接接触 Mapper 和持久化 Entity 的地方。
 * 查询结果会立即复制成 domain model，再交给 application 层；这样即使数据库表字段带有
 * tenantId、createDept 等平台遗留字段，也不会因为直接返回表实体而泄漏到接口契约。</p>
 */
@Repository
@RequiredArgsConstructor
public class MybatisLifeDataRepository implements LifeDataRepository {

    private final LmProjectMapper projectMapper;
    private final LmAbilityConfigMapper abilityConfigMapper;
    private final LmGameVersionMapper gameVersionMapper;
    private final LmGameActivityMapper gameActivityMapper;
    private final LmGameTargetMapper gameTargetMapper;
    private final LmTimelineEventMapper timelineEventMapper;
    private final LmMaterialMapper materialMapper;
    private final LmAlbumMapper albumMapper;
    private final LmPhotoMapper photoMapper;
    private final LmAssetMapper assetMapper;
    private final LmNoteMapper noteMapper;

    /** 查询项目基础信息，并把表实体转换为领域模型。 */
    @Override
    public LmProject findProjectById(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmProject.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmProject::getProjectId, projectId);
        return copy(projectMapper.selectOne(lqw), LmProject::new);
    }

    /** 查询项目下全部功能块配置。 */
    @Override
    public List<LmAbilityConfig> findAbilityConfigs(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig::getProjectId, projectId);
        return copyList(abilityConfigMapper.selectList(lqw), LmAbilityConfig::new);
    }

    /** 查询一个功能块配置，blockKey 对应 overview、targets、gallery 等能力块。 */
    @Override
    public LmAbilityConfig findAbilityConfig(Long projectId, String blockKey) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig::getProjectId, projectId);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig::getBlockKey, blockKey);
        return copy(abilityConfigMapper.selectOne(lqw), LmAbilityConfig::new);
    }

    /** 插入功能块配置前，把领域模型转换为 MyBatis 表实体。 */
    @Override
    public void insertAbilityConfig(LmAbilityConfig config) {
        var entity = copy(config, com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig::new);
        abilityConfigMapper.insert(entity);
    }

    /** 更新功能块配置前，把领域模型转换为 MyBatis 表实体。 */
    @Override
    public void updateAbilityConfig(LmAbilityConfig config) {
        var entity = copy(config, com.funmylife.fml.infrastructure.persistence.entity.LmAbilityConfig::new);
        abilityConfigMapper.updateById(entity);
    }

    /** 查询所有版本，不在仓储层做 UI 适配。 */
    @Override
    public List<LmGameVersion> findGameVersions(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion::getProjectId, projectId);
        return copyList(gameVersionMapper.selectList(lqw), LmGameVersion::new);
    }

    /** 当前版本用 status=active 判断，保持和旧服务口径一致。 */
    @Override
    public LmGameVersion findCurrentGameVersion(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion::getProjectId, projectId);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion::getStatus, "active");
        return copy(gameVersionMapper.selectOne(lqw), LmGameVersion::new);
    }

    /** 查询版本活动列表。 */
    @Override
    public List<LmGameActivity> findGameActivities(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmGameActivity.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmGameActivity::getProjectId, projectId);
        return copyList(gameActivityMapper.selectList(lqw), LmGameActivity::new);
    }

    /** 查询目标任务列表。 */
    @Override
    public List<LmGameTarget> findGameTargets(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmGameTarget.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmGameTarget::getProjectId, projectId);
        return copyList(gameTargetMapper.selectList(lqw), LmGameTarget::new);
    }

    /** 按主键读取目标，项目归属校验留给 application 层。 */
    @Override
    public LmGameTarget findGameTargetById(Long targetId) {
        return copy(gameTargetMapper.selectById(targetId), LmGameTarget::new);
    }

    /** 保存目标变更。 */
    @Override
    public void updateGameTarget(LmGameTarget target) {
        var entity = copy(target, com.funmylife.fml.infrastructure.persistence.entity.LmGameTarget::new);
        gameTargetMapper.updateById(entity);
    }

    /** 查询时间轴事件并按发生时间倒序返回，方便前端直接展示。 */
    @Override
    public List<LmTimelineEvent> findTimelineEvents(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmTimelineEvent.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmTimelineEvent::getProjectId, projectId);
        lqw.orderByDesc(com.funmylife.fml.infrastructure.persistence.entity.LmTimelineEvent::getOccurredAt);
        return copyList(timelineEventMapper.selectList(lqw), LmTimelineEvent::new);
    }

    /** 查询素材收集记录。 */
    @Override
    public List<LmMaterial> findMaterials(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmMaterial.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmMaterial::getProjectId, projectId);
        return copyList(materialMapper.selectList(lqw), LmMaterial::new);
    }

    /** 查询图册列表。 */
    @Override
    public List<LmAlbum> findAlbums(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmAlbum.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAlbum::getProjectId, projectId);
        return copyList(albumMapper.selectList(lqw), LmAlbum::new);
    }

    /** 统计图册数量，避免 application 层直接接触 Mapper count API。 */
    @Override
    public int countAlbums(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmAlbum.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAlbum::getProjectId, projectId);
        return albumMapper.selectCount(lqw).intValue();
    }

    /** 查询照片并按创建时间倒序返回。 */
    @Override
    public List<LmPhoto> findPhotos(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmPhoto.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmPhoto::getProjectId, projectId);
        lqw.orderByDesc(com.funmylife.fml.infrastructure.persistence.entity.LmPhoto::getCreateTime);
        return copyList(photoMapper.selectList(lqw), LmPhoto::new);
    }

    /** 查询账号资产列表。 */
    @Override
    public List<LmAsset> findAssets(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmAsset.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmAsset::getProjectId, projectId);
        return copyList(assetMapper.selectList(lqw), LmAsset::new);
    }

    /** 按主键读取资产，项目归属校验留给 application 层。 */
    @Override
    public LmAsset findAssetById(Long assetId) {
        return copy(assetMapper.selectById(assetId), LmAsset::new);
    }

    /** 查询笔记列表。 */
    @Override
    public List<LmNote> findNotes(Long projectId) {
        var lqw = Wrappers.lambdaQuery(com.funmylife.fml.infrastructure.persistence.entity.LmNote.class);
        lqw.eq(com.funmylife.fml.infrastructure.persistence.entity.LmNote::getProjectId, projectId);
        return copyList(noteMapper.selectList(lqw), LmNote::new);
    }

    /** 按主键读取笔记，项目归属校验留给 application 层。 */
    @Override
    public LmNote findNoteById(Long noteId) {
        return copy(noteMapper.selectById(noteId), LmNote::new);
    }

    /** 单对象属性复制：把 persistence entity 和 domain model 隔离开。 */
    private <T> T copy(Object source, Supplier<T> targetFactory) {
        if (source == null) {
            return null;
        }
        T target = targetFactory.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /** 列表属性复制的轻量封装，避免每个查询重复 stream 映射代码。 */
    private <T> List<T> copyList(List<?> source, Supplier<T> targetFactory) {
        return source.stream().map(item -> copy(item, targetFactory)).toList();
    }
}
