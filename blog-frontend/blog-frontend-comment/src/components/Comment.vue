<template>
  <div
    :class="[
      'halo-comment',
      { 'halo-comment__small': mergedConfigs.size === 'small' },
    ]"
    id="halo-comment"
  >
    <comment-editor
      v-if="isReply"
      :targetId="id"
      :target="target"
      :options="mergedOptions"
      :configs="mergedConfigs"
      class="bottom-comment"
    />
    <div class="comment-load-button" v-if="!mergedConfigs.autoLoad && !loaded">
      <a
        class="button-load"
        href="javascript:void(0)"
        rel="nofollow noopener"
        @click="loadComments"
        >加载评论</a
      >
    </div>
    <comment-loading v-show="commentLoading" :configs="mergedConfigs" />
    <ul class="commentwrap" v-if="comments.length >= 1">
      <template v-for="(comment, index) in comments">
        <CommentNode
          :targetId="id"
          :target="target"
          :comment="comment"
          :options="mergedOptions"
          :configs="mergedConfigs"
          :key="index"
          :depth="1"
        />
      </template>
    </ul>
    <div
      v-if="loaded && !commentLoading && comments.length <= 0"
      class="comment-empty"
    >
      {{ mergedConfigs.notComment || "暂无评论" }}
    </div>
    <div v-if="pagination.pages > 1" class="comment-page">
      <pagination
        :page="pagination.page"
        :size="pagination.size"
        :total="pagination.total"
        @change="handlePaginationChange"
      />
    </div>
    <!-- <img-previewer :visible.sync="showImgPreviewer" :url="previewImgUrl" /> -->
  </div>
</template>
<script>
/* eslint-disable no-unused-vars */
import Vue from "vue";
// import $ from "jquery";
import "./index";
import defaultConfig from "@/config/default_config";
import defaultOption from "@/config/default_option";
import commentApi from "../api/comment";
// import ImgPreviewer from "./ImgPreviewer";
// import optionApi from "../api/option";
import globals from "@/utils/globals.js";
import VueLazyload from "vue-lazyload";
import Tips from "@/plugins/Tips";
import { removeJsonEmpty, sleep } from "@/utils/util";
Vue.use(Tips);

export default {
  name: "Comment",
  props: {
    id: {
      type: Number,
      required: false,
      default: 0,
    },
    type: {
      type: String,
      required: false,
      default: "post",
      validator: function (value) {
        return ["post", "sheet", "journal", "links"].indexOf(value) !== -1;
      },
    },
    configs: {
      type: Object,
      required: false,
      default: () => defaultConfig,
    },
    options: {
      type: Object,
      required: false,
      default: () => defaultOption,
    },
  },
  // components: { ImgPreviewer },
  data() {
    return {
      comments: [],
      pagination: {
        pages: 0,
        page: 0,
        sort: "",
        size: 5,
        total: 0,
      },
      commentLoading: false,
      loaded: false,
      repliedSuccess: null,
      replyingComment: null,
      // options: {},
      globalData: globals,
      showImgPreviewer: false,
      previewImgUrl: "",
    };
  },
  computed: {
    target() {
      return `${this.type}s`;
    },
    mergedConfigs() {
      var jsonConfig;
      // 归一化配置数据
      if (typeof this.configs === "string") {
        const raws = JSON.parse(this.configs);
        const tmpObj = {};
        // 把字符串布尔值转为真实布尔值
        for (const key in raws) {
          const cur = raws[key];
          tmpObj[key] = /^(true|false)$/.test(cur) ? JSON.parse(cur) : cur;
        }
        jsonConfig = tmpObj;
      } else if (typeof this.configs === "object") {
        jsonConfig = this.configs;
      } else {
        throw new TypeError("config参数类型错误");
      }
      // 移除值为空的
      removeJsonEmpty(jsonConfig);

      return Object.assign(defaultConfig, jsonConfig);
    },
    mergedOptions() {
      var jsonOptions;
      // 归一化options数据
      if (typeof this.options === "string") {
        const raws = JSON.parse(this.options);
        const tmpObj = {};
        // 把字符串布尔值转为真实布尔值
        for (const key in raws) {
          const cur = raws[key];
          tmpObj[key] = /^(true|false)$/.test(cur) ? JSON.parse(cur) : cur;
        }
        jsonOptions = tmpObj;
      } else if (typeof this.options === "object") {
        jsonOptions = this.options;
      } else {
        throw new TypeError("options参数类型错误");
      }
      // 移除值为空的
      removeJsonEmpty(jsonOptions);

      return Object.assign(defaultOption, jsonOptions);
    },
    isReply() {
      return this.globalData.replyId == 0;
    },
  },
  beforeMount() {
    // await this.loadOptions();
    this.$nextTick(() => {
      Vue.use(VueLazyload, {
        error:
          this.mergedConfigs.avatarError,
        loading:
          this.mergedConfigs.avatarLoading,
        attempt: 1,
      });
    });
    if (this.mergedConfigs.autoLoad) {
      this.loadComments();
    }
  },
  // mounted() {
  //   $("[data-fancybox]").on("click", (e) => {
  //     this.previewImgUrl = e.target.src;
  //     this.showImgPreviewer = true;
  //   });
  // },
  methods: {
    loadComments() {
      this.comments = [];
      this.commentLoading = true;
      commentApi
        .listComments(this.target, this.id, "tree_view", this.pagination)
        .then(async (response) => {
          this.comments = response.data.data.content;
          this.pagination.size = response.data.data.rpp;
          this.pagination.total = response.data.data.total;
          this.pagination.pages = response.data.data.pages;
        })
        .finally(() => {
          this.commentLoading = false;
          this.loaded = true;
        });
    },
    // 现在直接从模板中获取了，不需要请求配置了
    // loadOptions() {
    //   return new Promise((resolve, reject) => {
    //     optionApi
    //       .list()
    //       .then((response) => {
    //         const resD = response.data.data;
    //         this.options = resD;
    //         resolve(resD);
    //       })
    //       .catch((err) => reject(err));
    //   });
    // },
    async handlePaginationChange(page) {
      this.pagination.page = page;
      await sleep(300);
      this.loadComments();
    },
  },
};
</script>
<style lang="scss">
$color: var(--theme);
@import "../styles/github-markdown";
@import "../styles/global";
</style>
