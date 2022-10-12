<!DOCTYPE html>
<html lang="zh-CN">
<#import "template/common/header.ftl" as headInfo>
<@headInfo.head title="所有标签" type="tags"/>
<#import "template/macro/tail.ftl" as tailInfo>
<body>
<div id="Joe">
    <#include "template/common/navbar.ftl">
    <div class="joe_container joe_main_container page-tags${settings.enable_show_in_up?then(' animated showInUp','')}${(settings.aside_position=='left')?then(' revert','')}">
        <div class="joe_main">
            <div class="joe_index">
                <div class="joe_index__title">
                    <ul class="joe_index__title-title pl-15">
                        <li class="item active">${settings.tags_title!}<@tagTag method="count"><span
                                    class="totals">${count!0}</span></@tagTag></li>
                    </ul>
                </div>
                <div class="joe_index__hot">
                    <ul class="joe_index__hot-list${(settings.tags_type!='card')?then('-tag','')} animated fadeIn"
                        style="padding-bottom: 10px;">
                        <@tagTag method="list">
                            <#list tags as tag>
                                <#if settings.tags_type=='card'>
                                    <li class="item">
                                        <a class="link" href="${tag.fullPath!}" title="${tag.name!}">
                                            <figure class="inner">
                                                <#if settings.enable_tags_post_num!true>
                                                    <em class="post-nums">${tag.postCount!}篇</em>
                                                </#if>
                                                <#assign thumbnail=(tag.thumbnail?? && tag.thumbnail!='')?then(tag.thumbnail,'') >
                                                <img width="100%" height="120" class="image lazyload"
                                                     data-src="${thumbnail}" src="${LAZY_IMG}"
                                                     onerror="Joe.errorImg(this,'${settings.fallback_thumbnail!}')"
                                                     alt="${tag.name!}">
                                                <figcaption class="title">${tag.name!}</figcaption>
                                            </figure>
                                        </a>
                                    </li>
                                <#else>
                                    <li class="item">
                                        <a class="link" href="${tag.fullPath!}" title="${tag.name!}">
                                            <span title="${tag.name!}">${tag.name!}</span>
                                            <#if settings.enable_tags_post_num!true>
                                                <em class="post-nums">${tag.postCount!}</em>
                                            </#if>
                                        </a>
                                    </li>
                                </#if>
                            </#list>
                        </@tagTag>
                    </ul>
                </div>
            </div>
        </div>
        <#if settings.enable_tags_aside!true>
            <#include "template/common/aside.ftl">
        </#if>
    </div>
    <#include "template/common/actions.ftl">
    <#include "template/common/footer.ftl">
</div>
<@tailInfo.tail type="tags"/>
</body>
</html>