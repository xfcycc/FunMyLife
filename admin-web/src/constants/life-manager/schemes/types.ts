export type SchemeCycleType = 'long_running' | 'stage_planned' | 'hybrid';

export type SchemeStatus = 'draft' | 'available' | 'archived';

export type SchemeSource = 'official' | 'user' | 'community' | 'ai_generated';

export type SchemeComplexity = 'light' | 'standard' | 'advanced';

export interface SchemeTemplate {
  schemeId: string;
  schemeName: string;
  type: {
    key: string;
    name: string;
  };
  cycleType: SchemeCycleType;
  version: string;
  status: SchemeStatus;
  source: SchemeSource;
  summary: string;
  targetUser: string;
  usageFrequency: string;
  complexity: SchemeComplexity;
  focusPoints: SchemeFocusPoint[];
  capabilities: SchemeCapabilityRef[];
  blockInstances: SchemeBlockInstance[];
  navigation: SchemeNavigationItem[];
  overviewRules: SchemeOverviewRule[];
  managementSections: SchemeManagementSection[];
  timelineRules: SchemeTimelineRule[];
  seedData: SchemeSeedData;
  safety: SchemeSafetyPolicy;
}

export interface SchemeFocusPoint {
  id: string;
  title: string;
  priority: 'high' | 'medium' | 'low';
  description: string;
}

export interface SchemeCapabilityRef {
  key: string;
  name: string;
  reason: string;
}

export interface SchemeBlockInstance {
  instanceId: string;
  blockKey: string;
  displayName: string;
  enabled: boolean;
  capabilities: string[];
  navigation: {
    visible: boolean;
    order: number;
  };
  fields?: SchemeFieldConfig[];
  behavior?: {
    resetRules?: unknown[];
    reminderRules?: unknown[];
    archiveRule?: unknown;
    statusFlow?: string[];
  };
  summaryRules?: SchemeOverviewRule[];
  timeline?: {
    enabled: boolean;
    defaultWriteRule: SchemeTimelineWriteRule;
  };
  aiRules?: SchemeAiRules;
  security?: SchemeBlockSecurity;
}

export interface SchemeFieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'select' | 'boolean' | 'progress';
  required?: boolean;
  options?: string[];
}

export interface SchemeNavigationItem {
  blockKey: string;
  label: string;
  order: number;
  visible: boolean;
}

export interface SchemeOverviewRule {
  id: string;
  source: string;
  enabled: boolean;
  title: string;
  maxItems: number;
  priority: number;
  filters: Record<string, unknown>;
  displayMode: 'metric' | 'list' | 'compact' | 'timeline';
  targetTab?: string;
}

export interface SchemeTimelineRule {
  id: string;
  sourceBlockKey: string;
  event: string;
  writeMode: SchemeTimelineWriteRule['mode'];
  displayInOverview: boolean;
  aiReadable: boolean;
  requireConfirm?: boolean;
}

export interface SchemeTimelineWriteRule {
  mode: 'none' | 'detail' | 'daily_summary' | 'weekly_summary' | 'exception_only';
  displayInOverview: boolean;
  aiReadable: boolean;
}

export interface SchemeAiRules {
  readable: boolean;
  writableAfterConfirm: boolean;
  allowedUse: string[];
}

export interface SchemeBlockSecurity {
  sensitivity: 'normal' | 'private' | 'sensitive';
  maskInOverview: boolean;
  requireConfirmBeforeExternalWrite: boolean;
}

export interface SchemeManagementSection {
  id: string;
  title: string;
  description: string;
  source: 'project' | 'block_instance' | 'business';
  blockKeys?: string[];
}

export interface SchemeSeedData {
  projectName: string;
  projectDescription: string;
  sampleOverviewTitles: string[];
  sampleTimelineTitles: string[];
}

export interface SchemeSafetyPolicy {
  sensitiveFields: string[];
  aiReadableByDefault: boolean;
  externalWritePolicy: 'confirm_required' | 'disabled' | 'allowed';
  notes: string[];
}
