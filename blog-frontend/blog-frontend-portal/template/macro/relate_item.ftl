<#macro relate_item posts curarticleId>
  <section class="joe_aside__item newest">
    <div class="joe_aside__item-title">
      <i class="joe-font joe-icon-huo"></i>
      <span class="text">相关文章</span>
    </div>
    <div class="joe_aside__item-contain"> 
      <#if posts?size gt 0>
        <ul class="list">       
          <#list posts as post>
            <#if (post_index lt settings.relate_post_max!5) && post.id != curarticleId>
              <li class="item">
                <a class="link" target="_blank" href="${post.fullPath!}" title="${post.title!}">${post.title!}</a>
                <i class="joe-font joe-icon-link"></i>
              </li>
            <#elseif posts?size == 1  && post.id == curarticleId>
              <#include "empty.ftl">
              <@empty type="relate" showImg="false" mini="true" />
            </#if>
          </#list>
        </ul>
      <#else>
        <#include "empty.ftl">
        <@empty type="relate" showImg="false" mini="true"/>
      </#if>
    </div>
  </section>
</#macro>