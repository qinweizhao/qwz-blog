// eslint-disable-next-line
import { BasicLayout, PageView, BlankLayout } from '@/layouts'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/dashboard',
    children: [
      // dashboard
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard'),
        meta: { title: '首页', icon: 'dashboard', hiddenHeaderContent: false, keepAlive: false }
      },

      // posts
      {
        path: '/post',
        name: 'Post',
        redirect: '/post/list',
        component: BlankLayout,
        meta: { title: '文章', icon: 'form' },
        children: [
          {
            path: '/post/list',
            name: 'PostList',
            component: () => import('@/views/post/PostList'),
            meta: { title: '全部', hiddenHeaderContent: false }
          },
          {
            path: '/post/write',
            name: 'PostEdit',
            component: () => import('@/views/post/PostEdit'),
            meta: { title: '新增', hiddenHeaderContent: false, keepAlive: false }
          },
          {
            path: '/category',
            name: 'CategoryList',
            component: () => import('@/views/post/CategoryList'),
            meta: { title: '分类', hiddenHeaderContent: false }
          },
          {
            path: '/tag',
            name: 'TagList',
            component: () => import('@/views/post/TagList'),
            meta: { title: '标签', hiddenHeaderContent: false }
          }
        ]
      },

      // journals
      {
        path: '/journal',
        name: 'JournalList',
        component: () => import('@/views/journal/JournalList'),
        meta: { title: '日志', icon: 'read' }
      },

      // comments
      {
        path: '/comment',
        name: 'Comment',
        component: () => import('@/views/comment/CommentList'),
        meta: { title: '评论', icon: 'message', hiddenHeaderContent: false }
      },

      // attachments
      {
        path: '/attachment',
        name: 'Attachment',
        component: () => import('@/views/attachment/AttachmentList'),
        meta: { title: '附件', icon: 'picture', hiddenHeaderContent: false }
      },

      // menu
      {
        path: '/menu',
        name: 'MenuList',
        component: () => import('@/views/menu/MenuList'),
        meta: { title: '菜单', icon: 'menu', hiddenHeaderContent: false }
      },

      // theme
      {
        path: '/theme',
        name: 'ThemeSetting',
        component: () => import('@/views/theme/ThemeSetting'),
        meta: { title: '主题', icon: 'skin' }
      },

      // user
      {
        path: '/user',
        name: 'User',
        component: PageView,
        redirect: '/user/profile',
        hidden: true,
        meta: { title: '用户', icon: 'user' },
        children: [
          {
            path: '/user/profile',
            name: 'Profile',
            component: () => import('@/views/user/Profile'),
            meta: { title: '个人资料', hiddenHeaderContent: false }
          }
        ]
      },

      // system
      {
        path: '/system',
        name: 'System',
        component: BlankLayout,
        redirect: '/system/config',
        meta: { title: '系统', icon: 'setting' },
        children: [
          {
            path: '/system/developer/config',
            name: 'DeveloperOptions',
            hidden: true,
            component: () => import('@/views/system/developer/DeveloperOptions'),
            meta: { title: '开发者选项', hiddenHeaderContent: false }
          },
          {
            path: '/system/config',
            name: 'SystemOptions',
            component: () => import('@/views/system/SystemOptions'),
            meta: { title: '设置', hiddenHeaderContent: false }
          },
          {
            path: '/system/tools',
            name: 'ToolList',
            component: () => import('@/views/system/ToolList'),
            meta: { title: '工具', hiddenHeaderContent: false }
          },
          {
            path: '/system/actionlogs',
            name: 'SystemActionLogs',
            hidden: true,
            component: () => import('@/views/system/ActionLogs'),
            meta: { title: '操作日志', hiddenHeaderContent: false }
          }
        ]
      }
    ]
  },
  {
    path: '/theme/setting/visual',
    name: 'ThemeVisualSetting',
    hidden: true,
    component: () => import('@/views/theme/ThemeVisualSetting'),
    meta: { title: '预览模式', hiddenHeaderContent: false }
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

export const constantRouterMap = [
  {
    path: '/login',
    name: 'Login',
    meta: { title: '登录' },
    component: () => import('@/views/user/Login')
  },
  {
    path: '/password/reset',
    name: 'ResetPassword',
    meta: { title: '重置密码' },
    component: () => import('@/views/user/ResetPassword')
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/exception/404')
  }
]
