<#if settings.enable_aside>
  <aside class="joe_aside${(settings.aside_position=='left')?then(' pos_left','')}">
      <#-- 展示博主信息 -->
      <#include "../module/blogger.ftl">

    <#if settings.site_notice??>
      <section class="joe_aside__item notice">
        <div class="joe_aside__item-title">
          <#include "../module/speaker.ftl">
          <span class="text">公告信息</span>
        </div>
        <div class="joe_aside__item-contain">
          <div class="notice_content">${settings.site_notice!}</div>
        </div>
      </section>
    </#if>
      <#import "../macro/latest.ftl" as np>
      <@np.newest/>
    <#if settings.enable_tag_cloud!true>
      <section class="joe_aside__item tags-cloud">
        <div class="joe_aside__item-title">
          <i class="joe-font joe-icon-tag"></i>
          <span class="text">标签云</span>
          <#-- 自适应使用 responsive，文件位置 global.less-->
          <#if tags?size gt settings.tag_cloud_max?default(18)?number><a class="tags_more" href="${blog_url!}/tags">更多<i class="joe-font joe-icon-more-right"></i></a></#if>
        </div>
        <div class="joe_aside__item-contain">
          <@tagTag method="list">
            <#if tags?size gt 0>
              <div class="tags-cloud-list" ${(settings.tag_cloud_type=='3d')?then(' style="display:none;"','')}>
                <#list tags as tag>
                  <#if tag_index lt settings.tag_cloud_max?default(18)?number>
                    <a data-url="${tag.fullPath!}" data-label="${tag.name!}" href="${tag.fullPath!}" title="${tag.name!}">${tag.name!}</a>
                  </#if>
                </#list>
              </div>
              <#if settings.tag_cloud_type=='3d'>
                <div id="tags-3d">
                  <div class="empty">加载中…</div>
                </div>
              </#if>
            <#else>
              <div class="empty">暂无标签</div>
            </#if>
          </@tagTag>
        </div>
      </section>
    </#if>
    <#if settings.enable_clean_mode!=true && settings.enable_aside_ads==true>
      <#include "../ads/ads_aside.ftl">
    </#if>
  </aside>
</#if>