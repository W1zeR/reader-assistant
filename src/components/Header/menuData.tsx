import { Menu } from "@/types/menu";

const menuData: Menu[] = [
  {
    id: 1,
    title: "Публичные цитаты",
    path: "/",
    newTab: false,
    isPrivate: false
  },
  {
    id: 2,
    title: "Личные цитаты",
    path: "/private",
    newTab: false,
    isPrivate: true
  },
  {
    id: 33,
    title: "Цитаты в ожидании опубликования",
    path: "/pending",
    newTab: false,
    isPrivate: true
  }
];
export default menuData;
