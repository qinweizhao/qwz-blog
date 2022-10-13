<#macro comment target type>
    <div class="joe_comment_box">
        <#if type=='post'><h2>评论区</h2></#if>
        <#assign sys_options = '{"blog_logo": "${options.blog_logo!}", "gravatar_source": "${options.gravatar_source!}", "comment_gravatar_default": "${options.comment_gravatar_default!}"}'>
        <#assign configs = '{"autoLoad": "${settings.enable_comment_autoload?string}", "showUserAgent": "${settings.enable_comment_ua?string}", "gravatarSource": "${options.gravatar_source!}", "loadingStyle": "${settings.comment_loading_style}", "authorPopup": "${settings.comment_author_poptext!}", "emailPopup": "${settings.comment_email_poptext!}", "aWord": "${settings.comment_aword!}", "avatarLoading": "${settings.comment_avatar_loading!}", "avatarError": "${settings.comment_avatar_error!}", "notComment": "${settings.comment_empty_text!}"}'>
        <halo-comment id='${target.id?c}' type='${type}' configs='${configs}' options='${sys_options}'/>
    </div>
</#macro>