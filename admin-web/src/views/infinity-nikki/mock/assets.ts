import type { AssetOverview } from '../types';

export const mockAssetOverview: AssetOverview = {
  total: 3,
  assets: [
    {
      id: 'a1',
      name: '官方账号 (主)',
      type: 'official_account',
      status: 'protected',
      statusLabel: '已保护',
      description: '主要游戏账号，已绑定手机和邮箱'
    },
    {
      id: 'a2',
      name: '小号-搭配测试',
      type: 'sub_account',
      status: 'protected',
      statusLabel: '已保护',
      description: '用于测试搭配方案的辅助账号'
    },
    {
      id: 'a3',
      name: 'Nintendo Switch 账号',
      type: 'switch_account',
      status: 'protected',
      statusLabel: '已保护',
      description: 'Switch 平台关联账号'
    }
  ]
};
