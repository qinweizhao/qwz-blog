<div class="swiper-slide">
  <a class="item clickable" href="${theme.repo + '/#readme'}" target="_blank" rel="noopener noreferrer nofollow">
    <img width="100%" height="100%" class="thumbnail lazyload" data-src="${BASE_RES_URL+'/source/img/dp/welcome.jpg'}" src="${settings.banner_lazyload_img}" onerror="Joe.errorImg(this)" alt="欢迎使用">
    <#if settings.enable_banner_title == true>
      <div class="title-row">
        <h3 class="title">欢迎使用 &nbsp;&nbsp;V${theme.version!}</h3>
      </div>
    </#if>
  </a>
</div>