export default {
  size: "normal", // 组件尺寸，normal/small
  autoLoad: true, // 是否自动加载评论
  showUserAgent: true, // 是否显示用户UserAgent信息
  gravatarType: "mm", // gravatar头像类型（可在后台管理里设置）
  gravatarSource: "", // gravatar头像源
  gravatarSourceDefault: "https://cn.gravatar.com/avatar", // gravatar默认头像源
  avatarError: `${process.env.BASE_URL}assets/img/default_avatar.jpg`, // 头像加载错误时展示的图片
  avatarLoading: `${process.env.BASE_URL}assets/svg/spinner-preloader.svg`, // 头像加载时展示的图片
  loadingStyle: "default", // 评论加载时的loading样式
  aWord: "你是我一生只会遇见一次的惊喜 ...", // 输入框聚焦时提示的一言
  authorPopup: "你的昵称是啥呢？", // 输入昵称时的提示文案
  emailPopup: "你将收到回复通知", // 输入邮箱时的提示文案
  urlPopup: "禁止小广告😀", // 输入网址时的提示文案
  notComment: "暂无评论", // 无数据时展示的文案
};
