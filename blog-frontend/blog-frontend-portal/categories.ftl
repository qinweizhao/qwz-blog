<!DOCTYPE html>
<html lang="zh-CN">
<#import "template/common/header.ftl" as headInfo>
<@headInfo.head title="文章分类" type="categorys"/>
<#import "template/macro/tail.ftl" as tailInfo>
<body>
<div id="Joe">
    <#include "template/common/navbar.ftl">
    <div class="joe_container joe_main_container page-categories${settings.enable_show_in_up?then(' animated showInUp','')}${(settings.aside_position=='left')?then(' revert','')}">
        <div class="joe_main">
            <div class="joe_index">
                <div class="joe_index__title">
                    <ul class="joe_index__title-title pl-15">
                        <li class="item active">全部分类<@categoryTag method="count"><span
                                    class="totals">${count!0}</span></@categoryTag></li>
                    </ul>
                </div>
                <div class="joe_index__hot categories">
                    <@categoryTag method="list">
                        <#if categories?size gt 0>
                            <ul class="joe_index__hot-list${(settings.categories_type!='card')?then('-tag','')} animated fadeIn">
                                <#list categories as category>
                                    <#if settings.categories_type=='card'>
                                        <li class="item">
                                            <a class="link" href="${category.fullPath!}" title="${category.name!}">
                                                <figure class="inner">
                                                    <#if settings.enable_categories_post_num!true>
                                                        <em class="post-nums">${category.postCount!}篇</em>
                                                    </#if>
                                                    <#assign thumbnail=(category.thumbnail?? && category.thumbnail!='')?then(category.thumbnail,'') >
                                                    <img width="100%" height="120" class="image lazyload"
                                                         data-src="${thumbnail}" src="${LAZY_IMG}"
                                                         onerror="Joe.errorImg(this,'${settings.fallback_thumbnail!}')"
                                                         alt="${category.name!}">
                                                    <figcaption class="title">${category.name!}</figcaption>
                                                </figure>
                                            </a>
                                        </li>
                                    <#else>
                                        <li class="item">
                                            <a class="link" href="${category.fullPath!}" title="${category.name!}">
                                                <span title="${category.name!}">${category.name!}</span>
                                                <#if settings.enable_categories_post_num!true>
                                                    <em class="post-nums">${category.postCount!}</em>
                                                </#if>
                                            </a>
                                        </li>
                                    </#if>
                                </#list>
                            </ul>
                        <#else>
                            <#include "template/macro/empty.ftl">
                            <@empty type="categories" text="未找到相关文章..."/>
                        </#if>
                    </@categoryTag>
                </div>
            </div>
        </div>
        <#if settings.enable_categories_aside!true>
            <#include "template/common/aside.ftl">
        </#if>
    </div>
    <#include "template/common/actions.ftl">
    <#include "template/common/footer.ftl">
</div>
<@tailInfo.tail type="categories"/>
</body>
</html>