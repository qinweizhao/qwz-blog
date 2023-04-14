<#-- 关键样式  -->
<style>
  @font-face {
    font-family: "Joe Font";
    font-weight: 400;
    font-style: normal;
    font-display: swap;
  }

  html body {
    --theme: ${settings.mode_color_light!"#fb6c28"};
    /*滚动条颜色*/
    --scroll-bar: #c0c4cc;
    --loading-bar: var(--theme);
    /*图片最大宽度*/
    --img-max-width: 100%;
    font-family: "Joe Font", "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, "sans-serif";
    --res-url: '${BASE_RES_URL}';
  }
  ::-webkit-scrollbar {
    width: 8px;
  }
  ::-webkit-scrollbar-thumb {
    background: var(--scroll-bar);
  }
  <#if settings.background_light_mode?? && settings.background_light_mode != "">
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