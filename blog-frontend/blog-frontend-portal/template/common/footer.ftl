<#if settings.enable_footer!true>
  <footer class="joe_footer${(settings.footer_position=='fixed')?then(' fixed','')}${settings.enable_full_footer?then(' full','')}">
      <div class="joe_container central">
        <div class="item">
          <p>
            ${.now?string("yyyy")} ©<a href="${blog_url!}" target="_blank" rel="noopener noreferrer">${user.nickname!}</a>
<#--            <#if settings.enable_icp && settings.icp?? && settings.icp?trim!=''>-->
              - <a class="icp" href="https://beian.miit.gov.cn" target="_blank" rel="noopener noreferrer nofollow">${settings.icp}</a>
<#--            </#if>-->
          </p>
          <#if settings.enable_police && settings.police?? && settings.police?trim!=''>
            <#assign recordcode=settings.police?replace('[^\\d]','','ri')>
            <p class="site_police">
              <a href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=${recordcode}" target="_blank" rel="noopener noreferrer nofollow">${settings.police}</a>
            </p>
          </#if>
          <#if settings.enable_sitemap==true>
            <div class="item">
              <#if settings.enable_sitemap><a href="${sitemap_xml_url!}" target="_blank" rel="noopener noreferrer">站点地图</a></#if>
            </div>
          </#if>
          <#if settings.enable_busuanzi!false>
            <div class="item busuanzi-statistic">
              <span class="site-pv"><i class="joe-font joe-icon-zhexiantu"></i>访问量<em id="busuanzi_value_site_pv">0</em></span>
              <span class="site-uv"><i class="joe-font joe-icon-monitor"></i>访客量<em id="busuanzi_value_site_uv">0</em></span>
              <span class="site-page-pv"><i class="joe-font joe-icon-dianji"></i>本页访客<em id="busuanzi_value_page_pv">0</em></span>
            </div>
          </#if>
        </div>
<#--        <div class="side-col">-->
<#--        <#if settings.enable_sitemap==true>-->
<#--          <div class="item">-->
<#--            <#if settings.enable_sitemap><a href="${sitemap_xml_url!}" target="_blank" rel="noopener noreferrer">站点地图</a></#if>-->
<#--          </div>-->
<#--        </#if>-->
<#--      </div>-->
      </div>
  </footer>
</#if>