const emojiData = require("../components/EmojiPicker/data/emojis.js");

export function renderedEmojiHtml(html) {
  const parser = new DOMParser();
  const doc = removeNotEmoji(parser.parseFromString(html, "text/html"));
  const emotions = doc.getElementsByClassName("emoji-animate");
  for (let i = 0; i < emotions.length; i++) {
    const emojiName = emotions[i].getAttribute("data-icon");
    for (let j = 0; j < emojiData["default"].length; j++) {
      const emoji = emojiData["default"][j];
      if (emoji.style && emoji.name === emojiName) {
        const img = emotions[i].getElementsByClassName("img")[0];
        let dataStyle = "";
        Object.keys(emoji.style).forEach(function (item) {
          dataStyle += item + ":" + emoji.style[item] + ";";
        });
        img.style.cssText = dataStyle;
        break;
      }
    }
  }
  return doc.body.innerHTML;
}
/**
 * 判断需要渲染的 HTML 是否属于表情包，如果不属于，则去除此 HTML，仅保留文字
 */
function removeNotEmoji(doc) {
  const smilies = doc.getElementsByClassName("emoji-img");
  let skip = true;
  for (let i = 0; i < smilies.length; i++) {
    const name = smilies[i].dataset.icon;
    if (!findEmoji(name)) {
      skip = false;
      break;
    }
  }

  if (skip) return doc;

  const firstNode = smilies[0];
  const name = firstNode.dataset.icon;

  !findEmoji(name) && removeNode(firstNode);

  return removeNotEmoji(doc);
}

function removeNode(node) {
  const alt = node.getAttribute("alt");
  const textNode = document.createTextNode(alt);
  node.parentNode.replaceChild(textNode, node);
}

/**
 * 根据条件查找表情
 * @param {*} type
 * @param {*} name
 * @returns 返回 true 则表示表情存在，返回 false 则表示不存在
 */
function findEmoji(name) {
  // const emojis = ["haha", "gulu", "tieba"];
  return emojiData["default"].filter(
    (item) => ["haha", "tieba"].includes(item.category) && item.name === name
  ).length > 0;
}