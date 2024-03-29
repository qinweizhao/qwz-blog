<!DOCTYPE html>
<html lang="zh-CN">
  <#import "template/common/header.ftl" as headInfo>
  <@headInfo.head title="${category.name!}" type="category"/>
  <#import "template/macro/tail.ftl" as tailInfo>
  <body>
    <div id="Joe">
      <#include "template/common/navbar.ftl">
      <div class="joe_container joe_main_container page-category${settings.enable_show_in_up?then(' animated showInUp','')}">
        <div class="joe_main">
          <div class="joe_archive">
            <div class="joe_archive__title">
              <div class="joe_archive__title-title">
                <i class="joe-font joe-icon-feather joe_archive__title-icon"></i>以下是
                <span class="muted ellipsis">${category.name!}</span>
                <span>相关的文章</span>
              </div>
            </div>
            <#if posts.content?size gt 0>
              <#include "template/macro/post_item.ftl">
              <ul class="joe_archive__list joe_list">
                <#list posts.content as post>
                  <@post_item post=post index=post_index type="category"/>
                </#list>
              </ul>
              <#include "template/common/pager.ftl">
              <@pager method="categoryPosts" postsData=posts slug="${category.slug!}" display="${settings.max_pager_number!5}" />
            <#else>
              <#include "template/macro/empty.ftl">
              <@empty type="category" text="未找到相关文章..."/>
            </#if>
          </div>
        </div>
      </div>
      <#include "template/common/actions.ftl">
      <#include "template/common/footer.ftl">
    </div>
    <@tailInfo.tail type="category"/> 
  </body>
</html>