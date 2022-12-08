<#macro tail type subType="">
  <#-- ===== 引入脚本 start ===== -->
  <#if (type == 'index' && settings.enable_index_list_effect == true) || ( type == 'journals' && settings.enable_journal_effect == true)>
    <script src="${BASE_RES_URL!}/source/lib/wowjs/wow.min.js"></script>
  </#if>
  <#if settings.show_newreply == true>
    <script src="${BASE_RES_URL}/source/lib/j-marked/marked.min.js"></script>
  </#if>
  <script src="${BASE_RES_URL!}/source/lib/lazysizes/lazysizes.min.js"></script>
  <script src="${BASE_RES_URL!}/source/lib/qmsg/qmsg.js"></script>
  <script src="${BASE_RES_URL}/source/js/min/utils.min.js?v=${theme.version!}"></script>
  <#if type == 'index' && settings.enable_banner == true>
    <script src="${BASE_RES_URL!}/source/lib/swiper/swiper.min.js"></script>
  </#if>
  <#if type == 'post' && settings.enable_toc == true>
    <script src="${BASE_RES_URL!}/source/lib/tocbot/tocbot.min.js"></script>
  </#if>
  <#if settings.enable_clean_mode != true && ( type == 'post' || type == 'journals') && subType != 'only_header_footer'>
    <script src="${BASE_RES_URL!}/source/lib/vue@2.6.10/vue.min.js"></script>
    <script src="${BASE_RES_URL!}/source/lib/halo-comment/halo-comment.min.js?v=${theme.version!}"></script>
  </#if>
  <script src="${BASE_RES_URL!}/source/lib/fancybox/jquery.fancybox.min.js"></script>
  <#assign enable_katex = settings.enable_katex?then('true','false')>
  <#if enable_katex == 'true' && (type == 'post' || type == 'journals')>
    <link rel="stylesheet" href="${BASE_RES_URL}/source/lib/katex@0.13.18/katex.min.css">
  </#if>
  <#-- 自定义 -->
  <script src="${BASE_RES_URL}/source/js/min/custom.min.js?v=${theme.version!}"></script>
  <#if type == 'post' || type == 'journals'>
    <script src="${BASE_RES_URL}/source/lib/clipboard/clipboard.min.js"></script>
  </#if>
  <#if settings.favicon?? && settings.favicon?trim!=''>
    <script src="${BASE_RES_URL}/source/lib/favico/favico.min.js"></script>
  </#if>
  <#if type == 'post'>
    <script src="${BASE_RES_URL}/source/lib/jquery-qrcode/jquery.qrcode.min.js"></script>
  </#if>

  <#-- ===== 引入页面级js start ===== -->
  <script src="${BASE_RES_URL}/source/js/min/common.min.js?v=${theme.version!}"></script>
  <#if type == 'post' || type == 'journals'>
    <script src="${BASE_RES_URL!}/source/lib/prism/prism.min.js"></script>
  </#if>
  <#if type == 'index'>
    <script src="${BASE_RES_URL}/source/js/min/index.min.js?v=${theme.version!}"></script>
  </#if>
  <#if type == 'archives'>
    <script src="${BASE_RES_URL}/source/js/min/archive.min.js?v=${theme.version!}"></script>
  </#if>
  <#if type == 'post'>
    <script src="${BASE_RES_URL}/source/js/min/post.min.js?v=${theme.version!}"></script>
  </#if>
  <#if type == 'journals'>
    <script src="${BASE_RES_URL}/source/js/min/journal.min.js?v=${theme.version!}"></script>
  </#if>
  <#-- ===== 引入页面级js end ===== -->

  <#-- ===== 引入脚本 start ===== -->
  <#if settings.enable_busuanzi!false>
    <!-- 卜算子 -->
    <script src="${BASE_RES_URL}/source/lib/busuanzi/busuanzi.min.js"></script>
  </#if>
  <#-- ===== 引入脚本 end ===== -->
  <#if settings.enable_debug>
    <!-- vconsole -->
    <script src="${BASE_RES_URL}/source/lib/vconsole/vconsole.min.js"></script>
  </#if>
</#macro>