<#macro favorite post type="bottom">
  <#if type="bottom">
    <div class="joe_detail__agree">
      <div class="agree">
        <div class="icon">
          <i class="joe-font joe-icon-like icon-like"></i>
          <i class="joe-font joe-icon-like-fill icon-unlike"></i>
        </div>
        <span class="nums">${post.likes!0}</span>
      </div>
    </div>
  <#else>
    <li class="post-operate-like">
<#--      <i class="joe-font joe-icon-dianzan icon-like"></i>-->
<#--      <i class="joe-font joe-icon-dianzan-fill icon-unlike"></i>-->
    </li>
  </#if>
</#macro>