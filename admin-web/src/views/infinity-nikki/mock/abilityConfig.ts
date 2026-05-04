import { infinityNikkiSchemeTemplate } from '@/constants/life-manager/schemes';
import type { SchemeBlockInstance, SchemeOverviewRule } from '@/constants/life-manager/schemes';
import type {
  AbilityBlockKey,
  AbilityInstanceConfig,
  AiRules,
  ArchiveRule,
  BlockSecurity,
  FieldConfig,
  OverviewSummaryRule,
  ReminderRule,
  ResetRule,
  TimelineWriteRule
} from '../types';

const PROJECT_ID = 'nikki-001';

function toOverviewSummaryRule(rule: SchemeOverviewRule): OverviewSummaryRule {
  return {
    id: rule.id,
    projectId: PROJECT_ID,
    source: rule.source as OverviewSummaryRule['source'],
    enabled: rule.enabled,
    title: rule.title,
    maxItems: rule.maxItems,
    priority: rule.priority,
    filters: rule.filters as OverviewSummaryRule['filters'],
    displayMode: rule.displayMode
  };
}

function toAbilityConfig(block: SchemeBlockInstance): AbilityInstanceConfig {
  return {
    id: block.instanceId,
    projectId: PROJECT_ID,
    blockKey: block.blockKey as AbilityBlockKey,
    displayName: block.displayName,
    enabled: block.enabled,
    capabilities: block.capabilities,
    navigation: block.navigation,
    summaryRules: (block.summaryRules ?? []).map(toOverviewSummaryRule),
    ...(block.fields ? { fields: block.fields as FieldConfig[] } : {}),
    ...(block.behavior
      ? {
          behavior: {
            ...(block.behavior.resetRules ? { resetRules: block.behavior.resetRules as ResetRule[] } : {}),
            ...(block.behavior.reminderRules ? { reminderRules: block.behavior.reminderRules as ReminderRule[] } : {}),
            ...(block.behavior.archiveRule ? { archiveRule: block.behavior.archiveRule as ArchiveRule } : {})
          }
        }
      : {}),
    ...(block.timeline
      ? {
          timeline: {
            enabled: block.timeline.enabled,
            defaultWriteRule: block.timeline.defaultWriteRule as TimelineWriteRule
          }
        }
      : {}),
    ...(block.aiRules ? { aiRules: block.aiRules as AiRules } : {}),
    ...(block.security ? { security: block.security as BlockSecurity } : {})
  };
}

export const mockAbilityInstanceConfigs: AbilityInstanceConfig[] =
  infinityNikkiSchemeTemplate.blockInstances.map(toAbilityConfig);
