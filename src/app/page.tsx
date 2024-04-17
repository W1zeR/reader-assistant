import ScrollUp from "@/components/Common/ScrollUp";

import Quotes from "@/components/Quotes";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "Помощник читателя | Публичные цитаты",
  description: "Это главная страница с публичными цитатами",
  // other metadata
};

export default function Home() {
  return (
    <>
      <ScrollUp />
      <Quotes />
    </>
  );
}
