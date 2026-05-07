package com.funmylife.fml.domain.capability;

import com.funmylife.fml.domain.block.BlockInstance;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 概览摘要构建上下文。
 *
 * <p>Overview application service 会把项目 ID、功能块实例和摘要规则放入该上下文，
 * 再交给实现 SummaryContributor 的能力。能力不应该绕过上下文直接读取 Controller 入参。</p>
 */
@Data
public class SummaryBuildContext {

    /** 当前项目 ID。 */
    private Long projectId;

    /** 当前正在贡献摘要的功能块实例。 */
    private BlockInstance blockInstance;

    /** 当前能力需要解释的摘要规则列表。 */
    private List<SummaryRule> rules = new ArrayList<>();
}
