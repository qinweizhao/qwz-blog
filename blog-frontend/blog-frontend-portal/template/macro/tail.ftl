<#macro tail type subType="">
  <#-- ===== 引入脚本 start ===== -->
  <#if (type == 'index' && settings.enable_index_list_effect == true)>
    <script src="${BASE_RES_URL!}/source/lib/wowjs/wow.min.js"></script>
  </#if>
  <script src="${BASE_RES_URL!}/source/lib/lazysizes/lazysizes.min.js"></script>
  <script src="${BASE_RES_URL!}/source/lib/qmsg/qmsg.js"></script>
  <script src="${BASE_RES_URL}/source/js/min/utils.min.js"></script>
  <#if type == 'index' && settings.enable_banner == true>
    <script src="${BASE_RES_URL!}/source/lib/swiper/swiper.min.js"></script>
  </#if>
    <script src="${BASE_RES_URL!}/source/lib/tocbot/tocbot.min.js"></script>
    <script src="${BASE_RES_URL!}/source/lib/fancybox/jquery.fancybox.min.js"></script>

  <#-- 数学公式 -->
<#--  <link rel="stylesheet" href="${BASE_RES_URL}/source/lib/katex@0.13.18/katex.min.css">-->

  <#-- 自定义 -->
<#--  <script src="${BASE_RES_URL}/source/js/min/custom.min.js"></script>-->
  <#if type == 'post'>
    <script src="${BASE_RES_URL}/source/lib/clipboard/clipboard.min.js"></script>
  </#if>
  <#if type == 'post'>
    <script src="${BASE_RES_URL}/source/lib/jquery-qrcode/jquery.qrcode.min.js"></script>
  </#if>

  <#-- ===== 引入页面级js start ===== -->
  <script src="${BASE_RES_URL}/source/js/min/common.min.js"></script>
  <#if type == 'post'>
    <script src="${BASE_RES_URL!}/source/lib/prism/prism.min.js"></script>
  </#if>
  <#if type == 'index'>
    <script src="${BASE_RES_URL}/source/js/min/index.min.js"></script>
  </#if>
  <#if type == 'archives'>
    <script src="${BASE_RES_URL}/source/js/min/archive.min.js"></script>
  </#if>
  <#if type == 'post'>
    <script src="${BASE_RES_URL}/source/js/min/post.min.js"></script>
  </#if>
  <#-- ===== 引入页面级js end ===== -->

  <#-- ===== 引入脚本 start ===== -->

    <!-- 卜算子 -->
    <script src="${BASE_RES_URL}/source/lib/busuanzi/busuanzi.min.js"></script>

  <#-- ===== 引入脚本 end ===== -->
  <#if settings.enable_debug>
    <!-- vconsole -->
    <script src="${BASE_RES_URL}/source/lib/vconsole/vconsole.min.js"></script>
  </#if>
</#macro>