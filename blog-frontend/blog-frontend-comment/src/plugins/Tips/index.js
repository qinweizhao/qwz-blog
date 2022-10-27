import Vue from "vue";
import Tips from "./index.vue";

let tipTem = Vue.extend(Tips);
let instance;
let timer = null;

const tips = (message, time = 3500, _self, type = 'warning') => {
  if (!instance) {
    instance = new tipTem();
    instance.vm = instance.$mount();
    /**
     * 解决提示信息不存在的问题，使用此方式后，样式只能由配置参数传入
     * 如果想使用主题自定义的方式，可以采用 var rootDom = document.body; 这时将采用主题定义的样式（默认样式会失效）
     */
    var rootDom = _self.$root.$el;
    rootDom.appendChild(instance.vm.$el);
  }

  if (timer) {
    clearTimeout(timer);
    timer = null;
    instance.show = false;
    instance.message = "";
  }
  instance.time = 3000;
  instance.type = type;
  if (typeof message === "string") {
    instance.message = message;
  } else {
    return;
  }

  if (typeof time === "number") {
    instance.time = time;
  }

  instance.show = true;
  timer = setTimeout(() => {
    instance.show = false;
    clearTimeout(timer);
    timer = null;
    instance.message = "";
  }, instance.time);
};

tips.install = (Vue) => {
  Vue.prototype.$tips = tips;
};

export default tips;