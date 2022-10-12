<#macro post_thumbnail post>
  <#assign thumbnail = post.thumbnail!?trim>
  <#assign default_img_url = settings.post_thumbnail>
  <#if thumbnail == ''>
    <#if post.categories?size gt 0>
      <#assign thumbnail = post.categories[0].thumbnail!?trim>
      <#if thumbnail == ''>
        <#if post.tags?size gt 0>
          <#assign thumbnail = post.tags[0].thumbnail!?trim>
          <#if thumbnail == ''>
            <#assign thumbnail = default_img_url>
          </#if>
        <#else>
          <#assign thumbnail = default_img_url>
        </#if>
      </#if>
    <#else>
      <#if post.tags?size gt 0>
        <#assign thumbnail = post.tags[0].thumbnail!?trim>
        <#if thumbnail == ''>
          <#assign thumbnail = default_img_url>
        </#if>
      <#else>
        <#assign thumbnail = default_img_url>
      </#if>
    </#if>
  </#if>
</#macro>