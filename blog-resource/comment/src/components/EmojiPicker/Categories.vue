<template>
  <transition name="category">
    <table class="motion-switcher-table">
      <tbody>
        <tr>
          <th
            v-for="(categorie, index) in categories"
            :class="[
              'category',
              categorie.name + '-box',
              { active: index === active },
              { 'on-hover': index === active },
            ]"
            :key="index"
            @click="onSelect(index)"
          >
            {{ categorie.title }}
          </th>
        </tr>
      </tbody>
    </table>
  </transition>
</template>

<script>
export default {
  name: "Categories",
  data: () => ({
    categories: [
      { name: 'haha', title: '泡泡' }
    ],
    active: 0,
  }),
  methods: {
    onSelect(index) {
      this.active = index;
      const _category = this.categories[index];
      this.$emit("select", _category);
    },
  },
};
</script>
