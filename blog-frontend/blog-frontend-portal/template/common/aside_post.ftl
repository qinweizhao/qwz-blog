<aside class="joe_aside">
  <#if settings.show_blogger!true>
    <#include "../module/blogger.ftl">
  </#if>
  <div class="joe_aside_post">
      <div class="toc-container">
        <h3 class="toc-header"><i class="joe-font joe-icon-xiaoxi" title="文章目录"></i>文章目录</h3>
        <div id="js-toc" class="toc"></div>
      </div>
    <#if settings.enable_relate_post!true>
      <#import "../macro/relate.ftl" as np>
      <@np.relate postData=post />
    </#if>
    <#if settings.enable_clean_mode!=true && settings.enable_aside_ads==true>
      <#include "../ads/ads_aside.ftl">
    </#if>
  </div>
</aside>