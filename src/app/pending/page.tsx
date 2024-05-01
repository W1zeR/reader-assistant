import { Metadata } from "next";
import PendingQuotes from "@/components/PendingQuotes";

export const metadata: Metadata = {
  title: "Помощник читателя | Цитаты в ожидании опубликования",
  description: "Это страница с цитатами в ожидании опубликования"
};

const Pending = () => {
  return (
    <>
      <PendingQuotes />
    </>
  );
};

export default Pending;
