<!DOCTYPE html>
<html lang="zh-CN">
<#assign title="我的动态">
<#import "template/common/header.ftl" as headInfo>
<@headInfo.head title="${title}" type="journals"/>
<#import "template/macro/tail.ftl" as tailInfo>
<#include "template/macro/loading.ftl">
<body>
<div id="Joe">
    <#include "template/common/navbar.ftl">
    <div class="joe_container joe_main_container page-journals${settings.enable_show_in_up?then(' animated showInUp','')}${(settings.aside_position=='left')?then(' revert','')}">
        <div class="joe_main">
            <div class="joe_detail">
                <h1 class="joe_detail__title txt-shadow">${title}</h1>
                <div class="joe_detail__count">
                    <div class="joe_detail__count-information">
                        <img width="35" height="35" class="avatar lazyload" data-src="${USER_AVATAR}"
                             src="${USER_AVATAR_LAZYLOAD}" onerror="Joe.errorImg(this)" alt="${user.nickname!}">
                        <div class="meta">
                            <div class="author">
                                <a class="link" href="#"
                                   title="${user.nickname!}">${user.nickname!}</a>
                            </div>
                        </div>
                    </div>
                </div>
                <@loading type="journals" />
                <#if journals.content?size gt 0>
                    <ul class="joe_journals__list hidden${settings.enable_journal_effect?then(' effects','')}">
                        <#list journals.content as journal>
                            <li class="joe_journal__item animated wow" data-wow-delay="0.${journal_index}s"
                                data-cid="${journal.id?c}" data-clikes="${journal.likes}">
                                <p class="joe_journal_date">
                                    <i class="joe-font joe-icon-feather"></i>
                                    <em class="joe_journal-posttime">${journal.createTime?date('yyyy-MM-dd')}</em>
                                </p>
                                <div class="joe_journal_block">
                                    <div class="joe_journal_body${(settings.enable_code_line_number==true)?then(' line-numbers','')}"
                                         style="max-height:${settings.journal_block_height!300}px">
                                        <div class="content-wrp">${journal.content!}</div>
                                        <span class="joe_journal_operate_item journal_content_expander"><i
                                                    class="joe-font joe-icon-arrow-down"></i></span>
                                    </div>

                                    <div class="joe_journal_footer">
                                        <div class="footer-wrap">
                                            <#-- 日志点赞 -->
                                            <span class="joe_journal_operate_item like">
                                                <i class="joe-font joe-icon-xihuan journal-like"></i>
                                                <i class="joe-font joe-icon-xihuan-fill journal-unlike"></i>
                                                <em class="journal-likes-num">${journal.likes!0}</em>
                                              </span>

<#--                                            <#if settings.enable_clean_mode!=true>-->
<#--                                                <span class="joe_journal_operate_item comment"><i-->
<#--                                                            class="joe-font joe-icon-message journal-comment"></i><em>${journal.commentCount!0}</em></span>-->
<#--                                                <#if journal.commentCount gt 0>-->
<#--                                                    <span class="joe_journal_operate_item journal_comment_expander"><em-->
<#--                                                                class="journal_comment_expander_txt">查看评论</em><i-->
<#--                                                                class="joe-font joe-icon-arrow-downb"></i></span>-->
<#--                                                </#if>-->
<#--                                            </#if>-->
<#--                                            <#if settings.enable_clean_mode!=true>-->
<#--                                                <div class="joe_journal_comment">-->
<#--                                                    <#assign sys_options = '{"blog_logo": "${options.blog_logo!}", "gravatar_source": "${options.gravatar_source!}", "comment_gravatar_default": "${options.comment_gravatar_default!}"}'>-->
<#--                                                    <#assign configs = '{"size": "small", "autoLoad": false, "gravatarSource": "${options.gravatar_source!}", "loadingStyle": "${settings.comment_loading_style!}", "authorPopup": "${settings.comment_author_poptext!}", "emailPopup": "${settings.comment_email_poptext!}", "aWord": "${settings.comment_aword!}", "avatarLoading": "${settings.comment_avatar_loading!}", "avatarError": "${settings.comment_avatar_error!}", "notComment": "${settings.comment_empty_text!}"}'>-->
<#--                                                    <halo-comment id="${journal.id?c}" type="journal"-->
<#--                                                                  configs='${configs}' options='${sys_options}'/>-->
<#--                                                </div>-->
<#--                                            </#if>-->
                                        </div>
                                    </div>

                                </div>
                            </li>
                        </#list>
                    </ul>
                    <#include "template/common/pager.ftl">
                    <@pager method="journals" postsData=journals display="${settings.max_pager_number!5}" />
                <#else>
                    <#include "template/macro/empty.ftl">
                    <@empty type="journals" text="暂无日志数据"/>
                </#if>
            </div>
        </div>
        <#include "template/common/aside.ftl">
    </div>
    <#include "template/common/actions.ftl">
    <#include "template/common/footer.ftl">
</div>
<@tailInfo.tail type="journals"/>
</body>
</html>