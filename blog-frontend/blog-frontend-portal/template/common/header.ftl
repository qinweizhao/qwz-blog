<#--  header 宏，接收参数 title:标题,type:来源页类型,id:自定义页面id  -->
<#macro head title type id=0>
  <head>
    <meta name="baidu-site-verification" content="code-EwVZfPhy0l" />
    <title>${(type == 'index')?then(blog_title!, title! + '-' + blog_title!)}</title>
    <#include "../config.ftl">
    <#include "../module/meta.ftl">
    <#include "../module/link.ftl">
    <@link type="${type}"/>
    <script src="${BASE_RES_URL}/source/lib/jquery@3.5.1/jquery.min.js"></script>
  </head>
</#macro>