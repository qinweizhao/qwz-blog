<!DOCTYPE html>
<html lang="zh-CN">
<#assign title=settings.archives_title!'文章归档'>
<#import "template/common/header.ftl" as headInfo>
<@headInfo.head title="${title}" type="archives"/>
<#import "template/macro/tail.ftl" as tailInfo>
<body>
<div id="Joe">
    <#include "template/common/navbar.ftl">
    <div class="joe_container joe_main_container page-archives${settings.enable_show_in_up?then(' animated showInUp','')}${(settings.aside_position=='left')?then(' revert','')}">
        <div class="joe_main">
            <div class="joe_index joe_archives__filing">
                <div class="title">${title}</div>
                <div class="content">
                    <div class="joe_archives__wrapper animated fadeIn">
                        <div class="joe_archives-title">
                            <#assign metric=settings.archives_timeline_metric!"month">
                            <i class="joe-font joe-icon-timeline"></i>时间轴<em>（${(metric=='year')?then('年','月')}）</em>
                        </div>
                        <ul class="joe_archives-timelist">
                            <@postTag method="archive" type="${metric}">
                                <#list archives as archive>
                                    <li class="item">
                                        <div class="wrapper">
                                            <div class="panel in">${archive.year?c}
                                                年<#if metric == 'month'> ${archive.month?c} 月</#if><i
                                                        class="joe-font joe-icon-arrow-down"></i></div>
                                            <ol class="panel-body">
                                                <#list archive.posts?sort_by("createTime")?reverse as post>
                                                    <li>
                                                        <a rel="noopener noreferrer" target="_blank"
                                                           title="${post.title!}"
                                                           href="${post.fullPath!}">${post.createTime?date('MM-dd')}
                                                            ：${post.title!}</a>
                                                    </li>
                                                </#list>
                                            </ol>
                                        </div>
                                    </li>
                                </#list>
                            </@postTag>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <#if settings.enable_archives_aside!false>
            <#include "template/common/aside.ftl">
        </#if>
    </div>
    <#include "template/common/actions.ftl">
    <#include "template/common/footer.ftl">
</div>
<@tailInfo.tail type="archives"/>
</body>
</html>