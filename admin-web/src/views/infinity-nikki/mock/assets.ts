import type { AssetOverview } from '../types';

export const mockAssetOverview: AssetOverview = {
  total: 3,
  assets: [
    {
      id: 'a1',
      projectId: 'nikki-001',
      name: '官方账号 (主)',
      type: 'official_account',
      category: 'account',
      value: '10086xxxx',
      status: 'protected',
      statusLabel: '已保护',
      sensitivity: 'sensitive',
      description: '主要游戏账号，已绑定手机和邮箱',
      timelineRule: { mode: 'exception_only', displayInOverview: false, aiReadable: false },
      createdAt: '2025-04-10T08:00:00',
      updatedAt: '2026-04-29T10:00:00'
    },
    {
      id: 'a2',
      projectId: 'nikki-001',
      name: '小号-搭配测试',
      type: 'sub_account',
      category: 'account',
      status: 'protected',
      statusLabel: '已保护',
      sensitivity: 'normal',
      description: '用于测试搭配方案的辅助账号',
      timelineRule: { mode: 'exception_only', displayInOverview: false, aiReadable: false },
      createdAt: '2025-06-15T08:00:00',
      updatedAt: '2026-04-20T10:00:00'
    },
    {
      id: 'a3',
      projectId: 'nikki-001',
      name: 'Nintendo Switch 账号',
      type: 'switch_account',
      category: 'account',
      status: 'protected',
      statusLabel: '已保护',
      sensitivity: 'normal',
      description: 'Switch 平台关联账号',
      linkedUrl: 'https://accounts.nintendo.com',
      timelineRule: { mode: 'exception_only', displayInOverview: false, aiReadable: false },
      createdAt: '2025-08-20T08:00:00',
      updatedAt: '2026-03-15T10:00:00'
    }
  ]
};
