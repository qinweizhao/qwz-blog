<section class="joe_aside__item author">
  <img width="100%" height="120" class="image lazyload" data-src="${(settings.author_bg!='')?then(settings.author_bg,BASE_RES_URL+'/source/img/author_bg.jpg')}" src="${EMPTY_IMG!}" onerror="Joe.errorImg(this)" alt="博主栏壁纸">
  <div class="user">
    <div class="avatar_wrapper ${settings.avatar_type!}">
      <img class="avatar lazyload" data-src="${USER_AVATAR}" src="${USER_AVATAR_LAZYLOAD}" onerror="Joe.errorImg(this)" alt="博主头像"/>
    </div>
    <a class="link" href="${blog_url!}" target="_blank" rel="noopener noreferrer nofollow">
      ${user.nickname!}
    </a>
      <#if settings.enable_day_words!false>
        <img class="motto_day_words" height="14" src="https://v2.jinrishici.com/one.svg?font-size=146&spacing=2&color=grey" onerror="Joe.errorImg(this)"/>
      </#if>
  </div>
  <div class="count">
    <#if settings.overview_type == 'A'>
      <@categoryTag method="count">
        <div class="item" title="累计分类数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>分类数</span>
        </div>
      </@categoryTag>
      <@tagTag method="count">
        <div class="item" title="累计标签数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>标签数</span>
        </div>
      </@tagTag>
      <@postTag method="count">
        <div class="item" title="累计文章数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>文章数</span>
        </div>
      </@postTag>
    <#elseif settings.overview_type == 'B'>
      <@categoryTag method="count">
        <div class="item" title="累计分类数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>分类数</span>
        </div>
      </@categoryTag>
      <@tagTag method="count">
        <div class="item" title="累计标签数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>标签数</span>
        </div>
      </@tagTag>
    <#elseif settings.overview_type == 'C'>
      <@categoryTag method="count">
        <div class="item" title="累计分类数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>分类数</span>
        </div>
      </@categoryTag>
      <@postTag method="count">
        <div class="item" title="累计文章数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>文章数</span>
        </div>
      </@postTag>
    <#elseif settings.overview_type == 'D'>
      <@tagTag method="count">
        <div class="item" title="累计标签数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>标签数</span>
        </div>
      </@tagTag>
      <@postTag method="count">
        <div class="item" title="累计文章数 ${count!'0'}">
          <span class="num">${count!'0'}</span>
          <span>文章数</span>
        </div>
      </@postTag>
    <#else>
    </#if>
  </div>
  <#if settings.enable_social!false>
    <#include "social.ftl">
  </#if>
</section>