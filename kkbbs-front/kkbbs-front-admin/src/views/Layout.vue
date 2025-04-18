<template>
  <div class="layout-body">
    <el-container>
      <el-aside class="aside" :style="{ width: asideWidth + 'px' }">
        <div class="logo">
          <span v-if="!menuCollapse">知享乐园后台管理</span>
        </div>
        <div class="menu-panel">
          <el-menu
            :default-openeds="defaultOpeneds"
            :collapse-transition="false"
            :collapse="menuCollapse"
            class="el-menu-vertical-demo"
            default-active="2"
            background-color="#3d3c4a"
            text-color="#fff"
            active-text-color="#fff"
            router
            :defaultActive="defaultActive"
          >
            <template v-for="item in menuList">
              <el-sub-menu :index="item.path" v-if="item.children">
                <template #title>
                  <i :class="['iconfont', item.icon]"></i>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
                <el-menu-item
                  :index="subItem.path"
                  v-for="subItem in item.children"
                >
                  <span class="menu-name">{{
                    subItem.menuName
                  }}</span></el-menu-item
                >
              </el-sub-menu>
              <el-menu-item :index="item.path" v-else>
                <i :class="['iconfont', item.icon]"></i>
                <template #title>
                  <span class="menu-name">{{ item.menuName }}</span>
                </template>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div
            :class="[
              'op-menu',
              'iconfont',
              menuCollapse ? 'icon-expand' : 'icon-collapse',
            ]"
            @click="opMenu"
          ></div>
          <div class="menu-bread">
            <el-breadcrumb>
              <template v-for="item in menuBreadCrumbList">
                <el-breadcrumb-item v-if="item.name">
                  {{ item.name }}
                </el-breadcrumb-item>
              </template>
            </el-breadcrumb>
          </div>
        </el-header>
        <el-main class="main-content">
          <div class="tag-content">
            <el-tabs
              type="border-card"
              v-model="defaultActive"
              @tab-change="tabClick"
              @edit="editTab"
            >
              <el-tab-pane
                :name="item.path"
                :label="item.menuName"
                :closable="tabList.length > 1"
                v-for="item in tabList"
              ></el-tab-pane>
            </el-tabs>
          </div>
          <div class="content-body">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from "vue-router";
import { ref, watch } from "vue";
const router = useRouter();
const route = useRoute();
//默认选中
const defaultActive = ref();
//aside宽度
const asideWidth = ref(250);
//默认展开的菜单
const defaultOpeneds = ref([]);
//系统菜单
const menuList = [
  {
    menuName: "内容管理",
    icon: "icon-article",
    path: "/forum",
    children: [
      {
        menuName: "资源管理",
        path: "/forum/article",
      },
      {
        menuName: "评论管理",
        path: "/forum/comment",
      },
      {
        menuName: "标签管理",
        path: "/forum/board",
      },
    ],
  },
  {
    menuName: "用户管理",
    icon: "icon-user",
    path: "/user",
    children: [
      {
        menuName: "用户列表",
        path: "/user/list",
      },
    ],
  },
  {
    menuName: "设置",
    icon: "icon-settings",
    path: "/settings",
    children: [
      {
        menuName: "系统设置",
        path: "/settings/sys",
      },
    ],
  },
];
//菜单转换为map
const menuMap = {};
const init = () => {
  menuList.forEach((item) => {
    defaultOpeneds.value.push(item.path);
    item.children.forEach((subItem) => {
      menuMap[subItem.path] = subItem;
    });
  });
};

init();
//收起关闭菜单
const menuCollapse = ref(false);
const opMenu = () => {
  menuCollapse.value = !menuCollapse.value;
  if (menuCollapse.value) {
    asideWidth.value = 63;
  } else {
    asideWidth.value = 250;
  }
};
//菜单面包屑
const menuBreadCrumbList = ref([]);

//tab操作
const tabList = ref([]);
const tabClick = (e) => {
  router.push(e);
};
const editTab = (targetKey, action) => {
  if (action !== "remove") {
    return;
  }
  let curlPath = defaultActive.value;
  let tabs = tabList.value;
  if (targetKey == defaultActive.value) {
    tabs.forEach((tab, index) => {
      if (tab.path === targetKey) {
        //如果删除的不是当前选中的tag，那么选中下一个或者前一个
        let nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          curlPath = nextTab.path;
        }
      }
    });
  }
  tabList.value = tabs.filter((tab) => tab.path !== targetKey);
  if (curlPath !== defaultActive.value) {
    router.push(curlPath);
  }
};

watch(
  () => route,
  (newVal, oldVal) => {
    defaultActive.value = route.path;
    menuBreadCrumbList.value = route.matched;
    let currentMenu = tabList.value.find((item) => {
      return item.path == defaultActive.value;
    });
    if (!currentMenu) {
      tabList.value.push(menuMap[defaultActive.value]);
    }
  },
  { immediate: true, deep: true }
);
</script>

<style lang="scss">
//菜单收起样式
.el-popper {
  border: none !important;
  .el-menu-item.is-active {
    background: var(--el-color-primary);
  }
  .el-menu-item:hover {
    color: #d8d8ee;
  }
  .el-menu--popup {
    padding: 0px;
  }
}
.layout-body {
  .aside {
    background: #3d3c4a;
    .logo {
      display: flex;
      height: 50px;
      color: #fff;
      background: #191828;
      align-items: center;
      font-size: 18px;
      padding-left: 5px;
    }
    .menu-panel {
      height: calc(100vh - 50px);
      .menu-name {
        padding-left: 10px;
      }
      //去除边框
      .el-menu {
        border-right: none;
      }
      //每个菜单的颜色
      .el-menu-item {
        background: #353544;
      }
      //选中的颜色
      .el-menu-item.is-active {
        color: #fff;
        background: var(--el-color-primary);
      }
      //鼠标移上去的颜色
      .el-menu-item:hover {
        color: #d8d8ee;
      }
    }
  }
  .header {
    background: #fff;
    border-bottom: 1px solid #ddd;
    height: 50px;
    line-height: 50px;
    padding: 0px 10px !important;
    display: flex;
    align-items: center;
    .op-menu {
      font-size: 20px;
      cursor: pointer;
      color: #3a3a40;
    }
    .menu-bread {
      margin-left: 10px;
    }
  }
  .main-content {
    padding: 0px;
    .tag-content {
      .el-tabs--border-card {
        border: none;
      }
      .el-tabs__content {
        display: none;
      }
    }
    .content-body {
      overflow: hidden;
      padding: 10px 10px 5px 10px;
    }
  }
}
</style>