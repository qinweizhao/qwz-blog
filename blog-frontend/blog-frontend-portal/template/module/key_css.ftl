<#-- 关键样式  -->
<style type="text/css">
  @font-face {
    font-family: "Joe Font";
    font-weight: 400;
    font-style: normal;
    font-display: swap;
    <#if settings.custom_font?? && settings.custom_font != "">
      <#if settings.custom_font?ends_with(".woff")>
        <#assign fontFormat="woff">
      <#elseif settings.custom_font?ends_with(".woff2")>
        <#assign fontFormat="woff2">
      <#elseif settings.custom_font?ends_with(".ttf")>
        <#assign fontFormat="truetype">
      <#elseif settings.custom_font?ends_with(".eot")>
        <#assign fontFormat="embedded-opentype">
      <#elseif settings.custom_font?ends_with(".svg")>
        <#assign fontFormat="svg">
      <#else>
        <#assign fontFormat="xxx">
      </#if>
      src: url("${settings.custom_font}") format("${fontFormat}");
    </#if>
  }
  <#if settings.loading_bar_color?trim != "">
    <#assign lbar_colors = settings.loading_bar_color?trim?split("|")>
    <#assign lbar_light_color = lbar_colors[0]?trim>
    <#assign lbar_dark_color = (lbar_colors[1]?? && lbar_colors[1]?trim != "")?then(lbar_colors[1]?trim, lbar_light_color)>
  </#if>
  html body {
    --theme: ${settings.mode_color_light!"#fb6c28"};
    /*滚动条颜色*/
    --scroll-bar: #c0c4cc;
    --loading-bar: ${lbar_light_color!"var(--theme)"};
    /*图片最大宽度*/
    --img-max-width: 100%;
    font-family: "Joe Font", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, "sans-serif";
    --res-url: '${BASE_RES_URL}';
  }
  ::-webkit-scrollbar {
    width: ${settings.scrollbar_width!"8px"};
  }
  ::-webkit-scrollbar-thumb {
    background: var(--scroll-bar);
  }
  <#if settings.enable_background_light == true && settings.background_light_mode?? && settings.background_light_mode != "">
    html[data-mode="light"] body {
      background-position: top center;
      background-attachment: fixed;
      background-repeat: no-repeat;
      background-size: cover;
      background-image: url(${settings.background_light_mode!});
    }
  <#else>
    html[data-mode="light"] body {
      background-image: none;
    }
  </#if>
</style>