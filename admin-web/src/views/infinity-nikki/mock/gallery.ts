import type { GalleryOverview } from '../types';

export const mockGalleryOverview: GalleryOverview = {
  albumCount: 8,
  photoCount: 126,
  albums: [
    { id: 'al1', name: '心愿原野探索', coverUrl: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 24 },
    { id: 'al2', name: '搭配竞技场', coverUrl: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 18 },
    { id: 'al3', name: '奇迹之旅', coverUrl: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 15 },
    { id: 'al4', name: '繁花季节', coverUrl: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 20 },
    { id: 'al5', name: '时装展示', coverUrl: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 22 },
    { id: 'al6', name: '风景截图', coverUrl: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 12 },
    { id: 'al7', name: '活动记录', coverUrl: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 8 },
    { id: 'al8', name: '其他', coverUrl: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', photoCount: 7 }
  ],
  recentPhotos: [
    { id: 'p1', albumId: 'al1', url: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', caption: '心愿原野全景', takenAt: '2026-04-29T21:40:00' },
    { id: 'p2', albumId: 'al2', url: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', caption: '竞技场精彩瞬间', takenAt: '2026-04-29T18:00:00' },
    { id: 'p3', albumId: 'al3', url: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', caption: '奇迹之旅关卡', takenAt: '2026-04-28T20:00:00' },
    { id: 'p4', albumId: 'al4', url: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80', caption: '繁花季节场景', takenAt: '2026-04-27T15:30:00' },
    { id: 'p5', albumId: 'al5', url: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=150&h=100&q=80', caption: '晨曦花园套装', takenAt: '2026-04-26T19:00:00' },
    { id: 'p6', albumId: 'al1', url: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=400&h=300&q=80', thumbnail: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', caption: '原野日落', takenAt: '2026-04-25T21:00:00' }
  ]
};
