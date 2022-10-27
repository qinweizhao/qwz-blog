/**  自定义 Emoji 数据生成，生成的数据位于同目录下的 _output.js
 * 得到 _output.js 后即可在 src/components/EmojiPicker/data/emojis.js 中添加相关表情配置。
 **/
const path = require("path");
const fs = require("fs");

const resolve = (pathname) => path.resolve(__dirname, pathname);

// eslint-disable-next-line no-unused-vars
class Emoji {
  constructor(name, description, category, style) {
    this.name = name;
    this.description = description;
    this.category = category;
    this.style = style;
  }
}

// 生成数据
const run = (emojiName) => {
  const config = {
    type: emojiName,
    path: path.resolve(process.cwd(), "public/assets/emoji/" + emojiName),
    output: resolve("_output.js"),
  };

  fs.readdir(resolve(config.path), (err, data) => {
    if (err) {
      throw new Error(err);
    }

    const emojis = data.filter((item) => item.includes("icon_"));
    const emojiData = emojis.map((item) => {
      const name = item.replace('icon_', '').replace(/\.(.+)$/, '');
      return `new Emoji('${name}', 'ZH-${name}', '${config.type}')`;
    });
    const emojiStr = `const ${config.type}Emoji = ${JSON.stringify(emojiData,null,2).replace(/"/g, "")}`.replace(/"/g, "").replace(/'/g, '"');
    console.log(emojiStr);

    // 写入文件
    fs.writeFile(config.output, emojiStr, "utf-8", (err) => {
      if (err) {
        throw new Error(err);
      }
      console.log("Emoji 生成完毕 😆😆😆");
    });
  });

}

// run('haha');
run('gulu');