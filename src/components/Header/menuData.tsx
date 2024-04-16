import { Menu } from "@/types/menu";

const menuData: Menu[] = [
  {
    id: 1,
    title: "Публичные цитаты",
    path: "/",
    newTab: false,
  },
  {
    id: 2,
    title: "Личные цитаты",
    path: "/private",
    newTab: false,
  },
  {
    id: 33,
    title: "Цитаты в ожидании опубликования",
    path: "/pending",
    newTab: false,
  },
];
export default menuData;
