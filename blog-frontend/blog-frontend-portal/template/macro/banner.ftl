<#macro banner>
  <#assign limit = (settings.banner_amount_limit!=0)?then(settings.banner_amount_limit,10)>
  <#assign is_empty = false>

  <#-- 数据为空时显示欢迎图  -->
  <div class="joe_index__banner">
    <div class="swiper-container">  
      <div class="swiper-wrapper">

          <#if settings.banner_data?? && settings.banner_data != ''>
            <#list settings.banner_data?split('=====') as banners>
              <#if (banners_index + 1) lte limit>
                <#assign banner = banners?split('-|||-')>
                <#assign cur_title = (banner[0]?? && banner[0]?trim!='')?then(banner[0]?replace('\n','')?replace('\r','')?trim,'')>
                <#assign cur_subtitle = (banner[1]?? && banner[1]?trim!='')?then(banner[1]?replace('\n','')?replace('\r','')?trim,'')>
                <#assign cur_img = (banner[2]?? && banner[2]!='')?then(banner[2]?replace('\n','')?replace('\r','')?trim,'')>
                <#assign cur_link = (banner[3]?? && banner[3]!='')?then(banner[3]?replace('\n','')?replace('\r','')?trim,'')>
                <#assign clickable = cur_link != '' && cur_link != '#'>
                <#if cur_title?index_of('欢迎使用') == -1>
                  <div class="swiper-slide">
                    <a class="item${clickable?then(' clickable','')}" href="${clickable?then(cur_link,'javascript:;')}" ${clickable?then('target="_blank"','')} rel="noopener noreferrer nofollow">
                      <img width="100%" height="100%" class="thumbnail lazyload" data-src="${cur_img!}" src="${BASE_RES_URL+'/source/img/lazyload_h.gif'}" onerror="Joe.errorImg(this)" alt="${cur_title!'banner'}">
                      <#if settings.enable_banner_title == true && cur_title!=''>
                        <div class="title-row">
                          <h3 class="title">${cur_title}</h3>
                          <#if cur_subtitle!=''>
                            <p class="subtitle">${cur_subtitle}</p>
                          </#if>
                        </div>
                      </#if>
                      <i class="joe-font joe-icon-zhifeiji"></i>
                    </a>
                  </div>
                <#else>
                  <#include "../module/banner_item_default.ftl">
                </#if>
              </#if>
            </#list>
          <#else>
            <#assign is_empty = true>
            <#include "../module/banner_item_default.ftl">
          </#if>
      </div>
      <#if settings.enable_banner_pagination == true && is_empty != true>
        <div class="swiper-pagination"></div>
      </#if>
      <#if settings.enable_banner_handle == true && settings.enable_banner_switch_button == true && is_empty != true>
        <div class="swiper-button-next"></div>
        <div class="swiper-button-prev"></div>
      </#if>
    </div>
  </div>
</#macro>