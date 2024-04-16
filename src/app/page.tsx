import ScrollUp from "@/components/Common/ScrollUp";

import Testimonials from "@/components/Testimonials";
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
      <Testimonials />
    </>
  );
}
