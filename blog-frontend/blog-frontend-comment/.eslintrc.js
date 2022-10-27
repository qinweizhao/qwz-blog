const isProd = ["production","wc"].includes(process.env.NODE_ENV);

module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: ["plugin:vue/essential", "eslint:recommended"],
  rules: {
    "no-console": isProd ? 2 : 0,
    "no-debugger": isProd ? 2 : 0,
    "no-console": 0,
    // 'no-debugger': 0
    // 'no-unused-vars': 0
  },
  parserOptions: {
    parser: "babel-eslint",
  },
};
